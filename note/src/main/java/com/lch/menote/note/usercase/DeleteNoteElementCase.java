package com.lch.menote.note.usercase;

import com.lch.menote.common.util.UiHandler;
import com.lch.menote.note.controller.NoteElementController;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/5/21.
 */

public class DeleteNoteElementCase implements Runnable {
    private final int position;
    private NoteElementController controller;
    private WeakReference<ControllerCallback<Void>> ref;

    public DeleteNoteElementCase(ControllerCallback<Void> cb, int position) {
        this.ref = new WeakReference<>(cb);
        controller = new NoteElementController();
        this.position = position;
    }

    @Override
    public void run() {
        final ResponseValue<Void> res = controller.delete(position);

        UiHandler.post(new Runnable() {
            @Override
            public void run() {
                ControllerCallback<Void> cb = ref.get();
                if (cb == null) {
                    return;
                }
                cb.onComplete(res);
            }
        });
    }
}
