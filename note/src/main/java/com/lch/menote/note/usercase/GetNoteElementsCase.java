package com.lch.menote.note.usercase;

import com.lch.menote.common.util.UiHandler;
import com.lch.menote.note.controller.NoteElementController;
import com.lch.menote.note.domain.NoteElement;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */

public class GetNoteElementsCase implements Runnable {
    private NoteElementController controller;
    private WeakReference<ControllerCallback<List<NoteElement>>> ref;

    public GetNoteElementsCase(ControllerCallback<List<NoteElement>> cb) {
        this.ref = new WeakReference<>(cb);
        controller = new NoteElementController();
    }

    @Override
    public void run() {
        final ResponseValue<List<NoteElement>> res = controller.getElements();

        UiHandler.post(new Runnable() {
            @Override
            public void run() {
                ControllerCallback<List<NoteElement>> cb = ref.get();
                if (cb == null) {
                    return;
                }
                cb.onComplete(res);
            }
        });
    }
}
