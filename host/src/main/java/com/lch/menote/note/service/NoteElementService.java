package com.lch.menote.note.service;

import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.datainterface.NoteElementSource;
import com.lch.menote.note.model.NoteElement;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class NoteElementService {

    private NoteElementSource memNoteElementSource = NoteModuleInjector.getINS().provideNoteElementSource();


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
