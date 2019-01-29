package com.lch.menote.note.presenter;

import android.content.Context;

import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.domain.DeleteCloudNoteCase;
import com.lch.menote.note.domain.GetCloudNotesCase;
import com.lch.menote.note.model.NoteModel;

import java.util.List;

/**
 * Created by lichenghang on 2019/1/29.
 */

public class NoteDetailPresenter {

    public interface MvpView {
        void showListNotes(List<NoteModel> datas);

        void showFail(String msg);

        void showLoading();

        void dismissLoading();

        void showNoMore();

        void showEmpty();
    }


    private final GetCloudNotesCase getCloudNotesCase = new GetCloudNotesCase(NoteModuleInjector.getINS().provideRemoteNoteSource());
    private final DeleteCloudNoteCase deleteCloudNoteCase = new DeleteCloudNoteCase(NoteModuleInjector.getINS().provideRemoteNoteSource());

    private Context context;
    private MvpView view;


    public NoteDetailPresenter(Context context, MvpView view) {
        this.context = context;
        this.view = view;
    }


}
