package com.lch.menote.note.controller;

import com.lch.menote.note.data.mem.MemNoteElementSource;
import com.lch.menote.note.domain.NoteElement;
import com.lch.netkit.common.mvc.ResponseValue;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class NoteElementController {

    private MemNoteElementSource memNoteElementSource = new MemNoteElementSource();


    public ResponseValue<Void> insertText(int position) {
        NoteElement noteElement = new NoteElement();
        noteElement.type = NoteElement.TYPE_TEXT;

        memNoteElementSource.save(noteElement, position);

        return new ResponseValue<>();
    }

    public ResponseValue<Void> insertImg(String path, int position) {
        NoteElement noteElement = new NoteElement();
        noteElement.type = NoteElement.TYPE_IMG;
        noteElement.path = path;

        memNoteElementSource.save(noteElement, position);

        return new ResponseValue<>();
    }

    public ResponseValue<List<NoteElement>> getElements() {
        ResponseValue<List<NoteElement>> res = new ResponseValue<>();
        res.data = memNoteElementSource.getElements();
        return res;
    }

    public ResponseValue<Void> delete(int position) {
        memNoteElementSource.delete(position);
        return new ResponseValue<>();
    }

    public ResponseValue<Void> setElements(List<NoteElement> datas) {
        memNoteElementSource.setElements(datas);
        return new ResponseValue<>();
    }
}
