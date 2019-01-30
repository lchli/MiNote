package com.lch.menote.note.service;

import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.datainterface.LocalNoteSource;
import com.lch.menote.note.model.NoteModel;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class LocalNoteService {


    private final LocalNoteSource localNoteSource = NoteModuleInjector.getINS().provideLocalNoteSource();

    public void saveLocalNote(final NoteModel note, final ControllerCallback<Void> cb) {
        new UseCase<NoteModel, Void>() {
            @Override
            protected ResponseValue<Void> execute(NoteModel parameters) {
                return localNoteSource.save(parameters);
            }
        }.invokeAsync(note, cb);
    }

    public void deleteLocalNote(final String noteId, final ControllerCallback<Void> cb) {
        new UseCase<String, Void>() {
            @Override
            protected ResponseValue<Void> execute(String parameters) {
                return localNoteSource.delete(parameters);
            }
        }.invokeAsync(noteId, cb);

    }


}
