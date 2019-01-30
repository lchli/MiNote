package com.lch.menote.note.presenter;

import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.datainterface.RemoteNoteSource;
import com.lch.menote.utils.MvpViewUtils;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * Created by Administrator on 2019/1/30.
 */

public class CloudNoteWriteController {
    private final RemoteNoteSource remoteNoteSource = NoteModuleInjector.getINS().provideRemoteNoteSource();

    public void deleteNote(final String noteId, final ControllerCallback<Void> cb) {
        final ControllerCallback<Void> callback = MvpViewUtils.newUiThreadProxy(cb);

        UseCase.executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                ResponseValue<Void> res = remoteNoteSource.delete(noteId);
                if (res.hasError()) {
                    callback.onError(res.code, res.getErrorMsg());
                    return;
                }

                callback.onSuccess(null);
            }
        });
    }
}
