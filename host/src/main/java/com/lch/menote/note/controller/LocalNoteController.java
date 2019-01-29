package com.lch.menote.note.controller;

import android.content.Context;

import com.lch.menote.note.data.DatabseNoteRepo;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.model.NotePinedData;
import com.lch.menote.note.data.response.QueryNoteResponse;
import com.lch.menote.note.data.mapper.ModelMapper;
import com.lch.menote.note.ui.LocalNoteListAdp;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.TaskExecutor;
import com.lch.netkit.common.tool.UiHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class LocalNoteController {


    private DatabseNoteRepo localNoteSource;

    public LocalNoteController(Context context) {
        localNoteSource = new DatabseNoteRepo(context);
    }


    public void getLocalNotesWithCat(final DatabseNoteRepo.LocalNoteQuery query, final ControllerCallback<List<Object>> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<List<Object>> r = getNotesWithCatImpl(query);
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(r);
                        }
                    }
                });

            }
        });
    }

    private ResponseValue<List<Object>> getNotesWithCatImpl(final DatabseNoteRepo.LocalNoteQuery query) {
        ResponseValue<List<Object>> res = new ResponseValue<>();

        ResponseValue<QueryNoteResponse> notesRes = localNoteSource.queryNotes(query);
        if (notesRes.hasError() || notesRes.data == null) {
            res.err = notesRes.err;
            return res;
        }

        List<NoteModel> notes = notesRes.data.data;
        if (notes == null || notes.isEmpty()) {
            return res;
        }

        List<Object> all = new ArrayList<>();
        res.data = all;

        String preType = "";

        for (NoteModel note : notes) {
            String currentType = note.type;
            if (!preType.equals(currentType)) {
                all.add(new NotePinedData(LocalNoteListAdp.VIEW_TYPE_PINED, currentType));
                preType = currentType;
            }

            all.add(note);
        }

        return res;
    }


    public void saveLocalNote(final NoteModel note, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                localNoteSource.save(note);
                //deleteUnusedImages(note);

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(new ResponseValue<Void>());
                        }
                    }
                });
            }
        });

    }

    public void deleteLocalNote(final NoteModel note, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                localNoteSource.delete(ModelMapper.from(note));

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(new ResponseValue<Void>());
                        }
                    }
                });
            }
        });

    }


}
