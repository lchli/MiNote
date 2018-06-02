package com.lch.menote.note.data.mem;

import com.lch.menote.note.domain.NoteElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class MemNoteElementRepo {

    private List<NoteElement> datas = new ArrayList<>();

    public void save(NoteElement e, int position) {
        if (position >= 0) {
            datas.add(position, e);
        } else {
            datas.add(e);
        }
    }

    public void delete(int position) {
        datas.remove(position);
    }


    public List<NoteElement> getElements() {
        return datas;
    }

    public void setElements(List<NoteElement> datas) {
        this.datas = datas;
    }
}
