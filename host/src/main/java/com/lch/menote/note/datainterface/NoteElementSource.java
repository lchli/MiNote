package com.lch.menote.note.datainterface;

import com.lch.menote.note.model.NoteElement;

import java.util.List;

/**
 * Created by Administrator on 2019/1/31.
 */

public interface NoteElementSource {

    void save(NoteElement e, int position);
    void delete(int position);
    List<NoteElement> getElements();
    void setElements(List<NoteElement> datas);
}
