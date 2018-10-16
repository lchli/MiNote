package com.lch.menote.note.controller;

import com.lch.menote.note.data.mem.MemNoteElementRepo;
import com.lch.menote.note.domain.NoteElement;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class NoteElementController {

    private MemNoteElementRepo memNoteElementSource = new MemNoteElementRepo();


    public void insertText(int position) {
        NoteElement noteElement = new NoteElement();
        noteElement.type = NoteElement.TYPE_TEXT;

        memNoteElementSource.save(noteElement, position);
    }

    public void insertImg(String path, int position) {
        NoteElement noteElement = new NoteElement();
        noteElement.type = NoteElement.TYPE_IMG;
        noteElement.path = path;

        memNoteElementSource.save(noteElement, position);

    }

    public void insertAudio(String path, int position) {
        NoteElement noteElement = new NoteElement();
        noteElement.type = NoteElement.TYPE_AUDIO;
        noteElement.path = path;

        memNoteElementSource.save(noteElement, position);

    }

    public void insertVideo(String path, int position) {
        NoteElement noteElement = new NoteElement();
        noteElement.type = NoteElement.TYPE_VIDEO;
        noteElement.path = path;

        memNoteElementSource.save(noteElement, position);

    }

    public List<NoteElement> getElements() {
        return memNoteElementSource.getElements();
    }

    public void delete(int position) {
        memNoteElementSource.delete(position);
    }

    public void setElements(List<NoteElement> datas) {
        memNoteElementSource.setElements(datas);
    }
}
