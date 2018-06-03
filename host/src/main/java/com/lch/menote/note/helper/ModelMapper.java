package com.lch.menote.note.helper;

import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteModel;

/**
 * Created by lichenghang on 2018/5/27.
 */

public final class ModelMapper {

    public static NoteModel from(Note note) {
        NoteModel model = new NoteModel();
        model.category = note.category;
        model.content = note.content;
        model.imagesDir = note.imagesDir;
        model.lastModifyTime = note.lastModifyTime;
        model.ShareUrl = null;
        model.thumbNail = note.thumbNail;
        model.title = note.title;
        model.type = note.type;
        model.uid = note.uid;
        model.userId = note.userId;


        return model;
    }

    public static Note from(NoteModel model) {
        Note note = new Note();
        note.category = model.category;
        note.content = model.content;
        note.imagesDir = model.imagesDir;
        note.lastModifyTime = model.lastModifyTime;
        note.thumbNail = model.thumbNail;
        note.title = model.title;
        note.type = model.type;
        note.uid = model.uid;

        return note;
    }
}
