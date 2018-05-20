package com.lch.menote.note.controller;

import com.lch.menote.note.data.mem.MemNoteElementSource;
import com.lch.menote.note.domain.NoteElement;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class NoteElementController {

    private MemNoteElementSource memNoteElementSource = new MemNoteElementSource();


    public void insertText() {
        NoteElement noteElement = new NoteElement();
        noteElement.type = NoteElement.TYPE_TEXT;

        memNoteElementSource.save(noteElement);
    }

    public void insertImg(String path) {
        NoteElement noteElement = new NoteElement();
        noteElement.type = NoteElement.TYPE_IMG;

        memNoteElementSource.save(noteElement);
    }

    public List<NoteElement> getElements(){
        return memNoteElementSource.getElements();
    }

    public void delete(NoteElement data ){
        memNoteElementSource.delete(data);
    }

    public void resetChecked(){
        List<NoteElement> datas = memNoteElementSource.getElements();
        for(NoteElement e:datas){
            e.isSelected=false;
        }
    }
}
