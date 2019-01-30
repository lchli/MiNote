package com.lch.menote.note.presenter;

import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.datainterface.LocalNoteSource;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.model.NotePinedData;
import com.lch.menote.note.ui.LocalNoteListAdp;
import com.lch.menote.utils.MvpViewUtils;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;
import com.lchli.utils.tool.ListUtils;

import java.util.ArrayList;
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

    private final LocalNoteSource localNoteSource = NoteModuleInjector.getINS().provideLocalNoteSource();
    private LocalNoteSource.LocalNoteQuery query = LocalNoteSource.LocalNoteQuery.newInstance();
    private MvpView view;


    public LocalNoteListPresenter(MvpView view) {
        this.view = MvpViewUtils.newUiThreadProxy(view);
    }

    public void getLocalNotesWithCat() {

        view.showLoading();
        UseCase.executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseValue<List<NoteModel>> notesRes = localNoteSource.queryNotes(query);
                    if (notesRes.hasError()) {
                        view.showFail(notesRes.getErrorMsg());
                        return;
                    }
                    final List<NoteModel> notes = notesRes.data;
                    if (ListUtils.isEmpty(notes)) {
                        view.showEmpty();
                        return;
                    }
                    List<Object> all = new ArrayList<>();
                    String preType = "";

                    for (NoteModel note : notes) {
                        String currentType = note.type;
                        if (!preType.equals(currentType)) {
                            all.add(new NotePinedData(LocalNoteListAdp.VIEW_TYPE_PINED, currentType));
                            preType = currentType;
                        }

                        all.add(note);
                    }

                    view.showListNotes(all);
                } finally {
                    view.dismissLoading();
                }


            }
        });
    }

    public void destroyView() {
        view = new MvpView() {
            @Override
            public void showListNotes(List<Object> datas) {

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
            public void showEmpty() {

            }
        };
    }

}
