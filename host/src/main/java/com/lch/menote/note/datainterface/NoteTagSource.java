package com.lch.menote.note.datainterface;

import com.lchli.arch.clean.ResponseValue;

import java.util.List;

/**
 * Created by Administrator on 2019/1/29.
 */

public interface NoteTagSource {
    ResponseValue<Void> addTag(String tag);

    ResponseValue<Void> removeTag(String tag);

    ResponseValue<List<String>> getAllTag();
}
