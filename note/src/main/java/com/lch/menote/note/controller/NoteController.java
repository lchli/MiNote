package com.lch.menote.note.controller;

import android.content.Context;

import com.lch.menote.common.util.UiHandler;
import com.lch.menote.note.TaskExecutor;
import com.lch.menote.note.data.NoteSource;
import com.lch.menote.note.data.db.DbNoteRepo;
import com.lch.menote.note.data.net.NetNoteRepo;
import com.lch.menote.note.domain.HeadData;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NotePinedData;
import com.lch.menote.note.helper.ConstantUtil;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class NoteController {


    private NoteSource localNoteSource;
    private NoteSource netNoteSource;

    public NoteController(Context context) {
        localNoteSource = new DbNoteRepo(context);
        netNoteSource = new NetNoteRepo();
    }


    public ResponseValue<List<Object>> getLocalNotesWithCat(String tag, String title, boolean sortTimeAsc, String useId) {
        return getNotesWithCatImpl(tag, title, sortTimeAsc, useId, localNoteSource);
    }

    private ResponseValue<List<Object>> getNotesWithCatImpl(String tag, String title, boolean sortTimeAsc, String useId, NoteSource source) {
        ResponseValue<List<Object>> res = new ResponseValue<>();

        ResponseValue<List<Note>> notesRes = source.queryNotes(tag, title, sortTimeAsc, useId);
        if (notesRes.hasError()) {
            res.err = notesRes.err;
            return res;
        }

        List<Note> notes = notesRes.data;
        if (notes == null || notes.isEmpty()) {
            return res;
        }

        List<Object> all = new ArrayList<>();
        res.data = all;

        all.add(new HeadData());


        String preType = "";

        for (Note note : notes) {
            String currentType = note.type;
            if (!preType.equals(currentType)) {
                all.add(new NotePinedData(ConstantUtil.VIEW_TYPE_PINED, currentType));
                preType = currentType;
            }

            all.add(note);
        }

        return res;
    }

    public ResponseValue<List<Note>> getLocalNotes(String tag, String title, boolean sortTimeAsc, String useId) {
        return localNoteSource.queryNotes(tag, title, sortTimeAsc, useId);
    }

    public ResponseValue<List<Note>> getCloudNotes(String tag, String title, boolean sortTimeAsc, String useId) {

        return netNoteSource.queryNotes(tag, title, sortTimeAsc, useId);

    }


    public ResponseValue<List<Object>> getCloudNotesWithCat(String tag, String title, boolean sortTimeAsc, String useId) {
        return getNotesWithCatImpl(tag, title, sortTimeAsc, useId, netNoteSource);
    }

    public void saveNote(final Note note, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                localNoteSource.save(note);

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
