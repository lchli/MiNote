package com.lch.menote.note.usercase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lch.menote.common.util.UiHandler;
import com.lch.menote.note.controller.NoteController;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class GetLocalNotesWithCatCase implements Runnable {

    private final String tag;
    private final String title;
    private final boolean sortTimeAsc;
    private final String useId;
    private NoteController noteController;
    private WeakReference<ControllerCallback<List<Object>>> ref;

    public GetLocalNotesWithCatCase(@NonNull ControllerCallback<List<Object>> cb, String tag, String title, boolean sortTimeAsc, String useId, Context context) {
        ref = new WeakReference<>(cb);
        this.tag = tag;
        this.title = title;
        this.sortTimeAsc = sortTimeAsc;
        this.useId = useId;
        noteController = new NoteController(context);
    }

    @Override
    public void run() {
        final ResponseValue<List<Object>> res = noteController.getLocalNotesWithCat(tag, title, sortTimeAsc, useId);

        UiHandler.post(new Runnable() {
            @Override
            public void run() {
                ControllerCallback<List<Object>> cb = ref.get();
                if (cb == null) {
                    return;
                }

                cb.onComplete(res);
            }
        });
    }
}
