package com.lch.menote.note.service;


import com.lch.menote.note.data.SpNoteTagRepo;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

import java.util.List;

public class NoteTagService {

    private SpNoteTagRepo mSpNoteTagRepo = new SpNoteTagRepo();

    public void addTag(final String tag, final ControllerCallback<Void> cb) {
        new UseCase<Void, Void>() {
            @Override
            protected ResponseValue<Void> execute(Void parameters) {
                return mSpNoteTagRepo.addTag(tag);
            }
        }.invokeAsync(null, cb);

    }

    public void deleteTag(final String tag, final ControllerCallback<Void> cb) {
        new UseCase<Void, Void>() {
            @Override
            protected ResponseValue<Void> execute(Void parameters) {
                return mSpNoteTagRepo.removeTag(tag);
            }
        }.invokeAsync(null, cb);

    }

    public void getAllTag(final ControllerCallback<List<String>> cb) {
        new UseCase<Void, List<String>>() {
            @Override
            protected ResponseValue<List<String>> execute(Void parameters) {
                return mSpNoteTagRepo.getAllTag();
            }
        }.invokeAsync(null, cb);

    }
}
