package com.lch.menote.note.presenter;

import android.support.annotation.Nullable;

import com.lch.menote.note.datainterface.LocalNoteSource;
import com.lch.menote.note.service.LocalNoteService;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.utils.tool.ListUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/1/30.
 */

public class LocalNoteListPresenter {
    public interface MvpView {
        void showListNotes(List<Object> datas);

        void showFail(String msg);

        void showLoading();

        void dismissLoading();

        void showEmpty();

    }

    private LocalNoteSource.LocalNoteQuery query = LocalNoteSource.LocalNoteQuery.newInstance();
    private MvpView view;
    private LocalNoteService localNoteService = new LocalNoteService();


    public LocalNoteListPresenter(MvpView view) {
        this.view = view;
    }

    public void getLocalNotesWithCat() {

        view.showLoading();
        localNoteService.getLocalNotesWithCat(query, new ControllerCallback<List<Object>>() {
            @Override
            public void onSuccess(@Nullable List<Object> objects) {
                if (ListUtils.isEmpty(objects)) {
                    view.showEmpty();
                } else {
                    view.showListNotes(objects);
                }

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
