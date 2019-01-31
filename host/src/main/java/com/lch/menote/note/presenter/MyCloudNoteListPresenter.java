package com.lch.menote.note.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lch.menote.R;
import com.lch.menote.note.data.NetNoteRepo;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.service.CloudNoteService;
import com.lch.menote.user.UserApiManager;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter可测试，但很难被复用。
 * Created by lichenghang on 2019/1/29.
 */

public class MyCloudNoteListPresenter {

    public interface MvpView {
        void showListNotes(List<NoteModel> datas);

        void showFail(String msg);

        void showLoading();

        void dismissLoading();

        void showNoMore();

        void showEmpty();
    }

    private int page;
    private static final int PAGE_SIZE = 20;
    private List<NoteModel> all = new ArrayList<>();
    private boolean isHaveMore = false;
    private NetNoteRepo.NetNoteQuery mQuery = NetNoteRepo.NetNoteQuery.newInstance()
            .addSort("updateTime", NetNoteRepo.NetNoteQuery.DIRECTION_ASC);
    private Context context;
    private MvpView view;
    private final CloudNoteService cloudNoteService = new CloudNoteService();


    public MyCloudNoteListPresenter(Context context, MvpView view) {
        this.context = context;
        this.view = view;
    }

    public void refresh() {
        isHaveMore = true;
        all.clear();
        page = 0;

        getCloudNotes();

    }


    public void loadMore() {
        if (!isHaveMore) {
            view.showNoMore();
            return;
        }

        getCloudNotes();
    }

    public void deleteNote(String noteId) {
        view.showLoading();
        cloudNoteService.deleteNote(noteId, new ControllerCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void aVoid) {
                view.dismissLoading();
                EventBusUtils.post(new CloudNoteListChangedEvent());
            }

            @Override
            public void onError(int code, String msg) {
                view.dismissLoading();
                view.showFail(msg);

            }
        });

    }


    private void getCloudNotes() {
        User se = UserApiManager.getINS().getSession();

        if (se == null) {
            view.showFail(context.getString(R.string.not_login));
            return;
        }

        mQuery.setPage(page).setPageSize(PAGE_SIZE).setUserId(se.uid);

        view.showLoading();

        cloudNoteService.getCloudNotes(mQuery, new ControllerCallback<List<NoteModel>>() {
            @Override
            public void onSuccess(@Nullable List<NoteModel> noteModels) {
                view.dismissLoading();

                if (!ListUtils.isEmpty(noteModels)) {
                    all.addAll(noteModels);
                    page++;
                } else {
                    isHaveMore = false;
                }

                final List<NoteModel> result = new ArrayList<>();
                result.addAll(all);

                if (ListUtils.isEmpty(result)) {
                    view.showEmpty();

                } else {
                    view.showListNotes(result);
                }

            }

            @Override
            public void onError(int code, String msg) {
                view.dismissLoading();
                view.showFail(msg);

            }
        });


    }

    public void destroyView() {
        view = new MvpView() {
            @Override
            public void showListNotes(List<NoteModel> datas) {

            }

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
            public void showNoMore() {

            }

            @Override
            public void showEmpty() {

            }
        };
    }
}
