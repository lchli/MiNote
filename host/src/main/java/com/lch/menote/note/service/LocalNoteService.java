package com.lch.menote.note.service;

import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.datainterface.LocalNoteSource;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.model.NotePinedData;
import com.lch.menote.note.ui.LocalNoteListAdp;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;
import com.lchli.utils.tool.ListUtils;

import java.util.ArrayList;
import java.util.List;

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

    public void getLocalNotesWithCat(final LocalNoteSource.LocalNoteQuery query, final ControllerCallback<List<Object>> cb) {
        new UseCase<Void, List<Object>>() {
            @Override
            protected ResponseValue<List<Object>> execute(Void parameters) {
                ResponseValue<List<NoteModel>> notesRes = localNoteSource.queryNotes(query);
                if (notesRes.hasError()) {
                    return new ResponseValue<List<Object>>().setErrorMsg(notesRes.getErrorMsg());
                }
                if (ListUtils.isEmpty(notesRes.data)) {
                    return new ResponseValue<>();
                }

                List<Object> all = new ArrayList<>();
                String preType = "";

                for (NoteModel note : notesRes.data) {
                    String currentType = note.type;
                    if (!preType.equals(currentType)) {
                        all.add(new NotePinedData(LocalNoteListAdp.VIEW_TYPE_PINED, currentType));
                        preType = currentType;
                    }

                    all.add(note);
                }

                return new ResponseValue<List<Object>>().setData(all);

            }
        }.invokeAsync(null, cb);
    }


}
