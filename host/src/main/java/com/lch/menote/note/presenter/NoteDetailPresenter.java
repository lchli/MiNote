package com.lch.menote.note.presenter;

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.datainterface.RemoteNoteSource;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.model.NoteElement;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.ui.EditNoteUi;
import com.lch.menote.user.UserApiManager;
import com.lch.menote.user.route.User;
import com.lch.menote.utils.MvpUtils;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;
import com.lchli.utils.tool.AliJsonHelper;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.ListUtils;

import java.util.List;

/**
 * Created by lichenghang on 2019/1/29.
 */

public class NoteDetailPresenter {

    public interface MvpView {

        void showFail(String msg);

        void showLoading();

        void dismissLoading();

        void showEmpty();

        void showNoteContent(List<NoteElement> elements);

        void showActionBarTitle(String text);

        void showLikeMenuIcon(int resid);

        void removeMenue(int id);

        void finishUi();
    }

    public static final int LAUNCH_FROM_LOCAL_NOTE = 1;
    public static final int LAUNCH_FROM_CLOUD_NOTE = 2;
    private final RemoteNoteSource remoteNoteSource = NoteModuleInjector.getINS().provideRemoteNoteSource();
    private Context context;
    private MvpView view;
    private int launchFrom;
    private NoteModel model;


    public NoteDetailPresenter(Context context, MvpView view) {
        this.context = context;
        this.view = MvpUtils.newUiThreadProxy(view);
    }


    public void onOptionsItemSelected(int id) {
        if (id == R.id.action_edit_note) {
            EditNoteUi.launch(context, model);
            view.finishUi();
        } else if (id == android.R.id.home) {
            view.finishUi();
        } else if (id == R.id.action_share_note) {
            try {
                String path = model.ShareUrl;
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("text/plain");
                imageIntent.putExtra(Intent.EXTRA_TEXT, path);
                imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(imageIntent, "分享"));
            } catch (Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort(e.getLocalizedMessage());
            }
        } else if (id == R.id.action_public_note) {
            publicNetNote();
        } else if (id == R.id.action_like_note) {
            likeNetNote();
        }
    }

    public void initLoad(final Intent intent) {
        model = (NoteModel) intent.getSerializableExtra("note");
        launchFrom = intent.getIntExtra("from", LAUNCH_FROM_LOCAL_NOTE);

        if (model == null) {
            view.showEmpty();
            return;
        }
        view.showActionBarTitle(model.title);

        view.showLoading();
        UseCase.executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                List<NoteElement> datas = AliJsonHelper.parseArray(model.content, NoteElement.class);
                if (ListUtils.isEmpty(datas)) {
                    view.showEmpty();
                } else {
                    view.showNoteContent(datas);
                }
                view.dismissLoading();
            }
        });


    }

    public void onCreateOptionsMenu() {
        if (model == null) {
            return;
        }

        if (launchFrom == LAUNCH_FROM_LOCAL_NOTE) {
            view.removeMenue(R.id.action_like_note);
            view.removeMenue(R.id.action_share_note);
            view.removeMenue(R.id.action_public_note);
        } else if (launchFrom == LAUNCH_FROM_CLOUD_NOTE) {
            view.removeMenue(R.id.action_edit_note);
        }

        User se = UserApiManager.getINS().getSession();
        if (se == null || !se.uid.equals(model.userId) || model.isPublic()) {
            view.removeMenue(R.id.action_public_note);
        }

        if (model.star == null || se == null || !model.star.contains(se.uid)) {
            view.showLikeMenuIcon(R.drawable.like_thumb_up);
        } else {
            view.showLikeMenuIcon(R.drawable.like_thumb_up_selected);
        }

    }

    public void publicNetNote() {
        if (model == null) {
            return;
        }
        if (model.isPublic()) {
            view.showFail("已经公开");
            return;
        }
        User se = UserApiManager.getINS().getSession();
        if (se == null) {
            view.showFail("未登陆");
            return;
        }
        if (!se.uid.equals(model.userId)) {
            view.showFail("无权限");
            return;
        }

        view.showLoading();
        UseCase.executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                ResponseValue<NoteModel> res = remoteNoteSource.publicNote(model.uid);
                if (res.hasError()) {
                    view.showFail(res.getErrorMsg());
                    view.dismissLoading();
                    return;
                }
                if (res.data == null) {
                    view.showFail("data is null.");
                    view.dismissLoading();
                    return;
                }
                model = res.data;

                onCreateOptionsMenu();
                List<NoteElement> datas = AliJsonHelper.parseArray(model.content, NoteElement.class);
                if (ListUtils.isEmpty(datas)) {
                    view.showEmpty();
                } else {
                    view.showNoteContent(datas);
                }
                EventBusUtils.post(new CloudNoteListChangedEvent());

                view.dismissLoading();

            }
        });

    }


    public void likeNetNote() {
        if (model == null) {
            return;
        }
        User se = UserApiManager.getINS().getSession();
        if (se == null) {
            view.showFail("未登陆");
            return;
        }

        view.showLoading();
        UseCase.executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                ResponseValue<NoteModel> res = remoteNoteSource.likeNote(model.uid);
                if (res.hasError()) {
                    view.showFail(res.getErrorMsg());
                    view.dismissLoading();
                    return;
                }
                if (res.data == null) {
                    view.showFail("data is null.");
                    view.dismissLoading();
                    return;
                }
                model = res.data;

                onCreateOptionsMenu();
                List<NoteElement> datas = AliJsonHelper.parseArray(model.content, NoteElement.class);
                if (ListUtils.isEmpty(datas)) {
                    view.showEmpty();
                } else {
                    view.showNoteContent(datas);
                }
                EventBusUtils.post(new CloudNoteListChangedEvent());

                view.dismissLoading();

            }
        });

    }


    public void destroyView() {
        view = new MvpView() {
            @Override
            public void showFail(String msg) {

            }

            @Override
            public void showLoading() {

            }

            @Override
            public void dismissLoading() {

            }

            @Override
            public void showEmpty() {

            }

            @Override
            public void showNoteContent(List<NoteElement> elements) {

            }

            @Override
            public void showActionBarTitle(String text) {

            }

            @Override
            public void showLikeMenuIcon(int resid) {

            }

            @Override
            public void removeMenue(int id) {

            }

            @Override
            public void finishUi() {

            }
        };
    }
}
