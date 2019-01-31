package com.lch.menote.note.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lch.menote.R;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.events.LocalNoteListChangedEvent;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.service.CloudNoteService;
import com.lch.menote.note.service.LocalNoteService;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.utils.tool.EventBusUtils;

/**
 * Created by Administrator on 2019/1/31.
 */

public class LocalNoteAdpPresenter {
    public interface MvpView {

        void showFail(String msg);

        void showLoading();

        void dismissLoading();

        void toast(String msg);

    }

    private MvpView view;
    private final CloudNoteService cloudNoteService = new CloudNoteService();
    private final LocalNoteService localNoteService = new LocalNoteService();
    private Context context;

    public LocalNoteAdpPresenter(Context context, MvpView view) {
        this.view = view;
        this.context = context;
    }

    public void onUploadNote(NoteModel model) {
        view.showLoading();
        cloudNoteService.uploadNote(model, new ControllerCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void aVoid) {
                view.toast(context.getString(R.string.upload_note_success));
                EventBusUtils.post(new CloudNoteListChangedEvent());

                view.dismissLoading();
            }

            @Override
            public void onError(int code, String msg) {
                view.dismissLoading();
                view.showFail(msg);

            }
        });
    }


    public void onDeleteLocalNote(String noteId) {
        view.showLoading();
        localNoteService.deleteLocalNote(noteId, new ControllerCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void aVoid) {
                view.toast("删除成功");
                EventBusUtils.post(new LocalNoteListChangedEvent());

                view.dismissLoading();
            }

            @Override
            public void onError(int code, String msg) {
                view.dismissLoading();
                view.showFail(msg);

            }
        });
    }
}
