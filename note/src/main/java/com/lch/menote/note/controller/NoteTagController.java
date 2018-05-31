package com.lch.menote.note.controller;

import com.lch.menote.common.util.UiHandler;
import com.lch.menote.note.TaskExecutor;
import com.lch.menote.note.data.sp.SpNoteTagRepo;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;

import java.util.List;

public class NoteTagController {

    private SpNoteTagRepo mSpNoteTagRepo = new SpNoteTagRepo();

    public void addTag(final String tag, final ControllerCallback<Void> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<Void> res = mSpNoteTagRepo.addTag(tag);
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(res);
                    }
                });
            }
        });
    }

    public void deleteTag(final String tag, final ControllerCallback<Void> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<Void> res = mSpNoteTagRepo.removeTag(tag);
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(res);
                    }
                });
            }
        });
    }

    public void getAllTag(final ControllerCallback<List<String>> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<List<String>> res = mSpNoteTagRepo.getAllTag();
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(res);
                    }
                });
            }
        });
    }
}
