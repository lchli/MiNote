package com.lch.menote.note.data.mem;

import com.lch.menote.note.domain.NoteElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class MemNoteElementSource {

    private List<NoteElement> datas=new ArrayList<>();

    public void save(NoteElement e){
        datas.add(e);
    }

    public void delete(NoteElement e){
        datas.remove(e);
    }


    public List<NoteElement> getElements(){
        return datas;
    }
}
