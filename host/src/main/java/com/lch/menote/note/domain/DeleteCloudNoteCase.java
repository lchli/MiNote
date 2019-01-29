package com.lch.menote.note.domain;

import com.lch.menote.note.datainterface.RemoteNoteSource;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * Created by lichenghang on 2019/1/29.
 */

public class DeleteCloudNoteCase extends UseCase<DeleteCloudNoteCase.Param, Void> {

    public static class Param {
        public String noteId;
    }

    private RemoteNoteSource remoteNoteSource;

    public DeleteCloudNoteCase(RemoteNoteSource remoteNoteSource) {
        this.remoteNoteSource = remoteNoteSource;
    }

    @Override
    protected ResponseValue<Void> execute(DeleteCloudNoteCase.Param parameters) {
        return remoteNoteSource.delete(parameters.noteId);
    }
}
