package com.lch.menote.note.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lch.menote.note.data.NetNoteRepo;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.model.CloudNoteModel;
import com.lch.menote.note.service.CloudNoteService;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter可测试，但很难被复用。
 * Created by lichenghang on 2019/1/29.
 */

public class HotNoteListPresenter {

    public interface MvpView {
        void showListNotes(List<Object> datas);

        void showFail(String msg);

        void showLoading();

        void dismissLoading();

        void showNoMore();

        void showEmpty(boolean b);
    }

    private int page;
    private static final int PAGE_SIZE = 20;
    private List<CloudNoteModel> all = new ArrayList<>();
    private boolean isHaveMore = false;
    private NetNoteRepo.NetNoteQuery mQuery = NetNoteRepo.NetNoteQuery.newInstance()
            .addSort("updateTime", NetNoteRepo.NetNoteQuery.DIRECTION_ASC);
    private Context context;
    private MvpView view;
    private final CloudNoteService cloudNoteService = new CloudNoteService();


    public HotNoteListPresenter(Context context, MvpView view) {
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
            view.dismissLoading();
            view.showNoMore();
            return;
        }

        getCloudNotes();
    }

    public void deleteNote(String noteId) {
        view.showLoading();
        cloudNoteService.deleteNetNote(noteId, new ControllerCallback<Void>() {
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

        mQuery.setPage(page).setPageSize(PAGE_SIZE);

        view.showLoading();

        cloudNoteService.getCloudNotes(mQuery, new ControllerCallback<List<CloudNoteModel>>() {
            @Override
            public void onSuccess(@Nullable List<CloudNoteModel> noteModels) {
                view.dismissLoading();

                if (!ListUtils.isEmpty(noteModels)) {
                    all.addAll(noteModels);
                    page++;
                } else {
                    isHaveMore = false;
                }

                final List<Object> result = new ArrayList<>();
                result.addAll(all);

                if (ListUtils.isEmpty(result)) {
                    view.showEmpty(true);

                } else {
                    view.showEmpty(false);
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

}
