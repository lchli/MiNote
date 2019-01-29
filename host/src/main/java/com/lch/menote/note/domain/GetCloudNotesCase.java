package com.lch.menote.note.domain;

import com.lch.menote.note.datainterface.RemoteNoteSource;
import com.lch.menote.note.model.NoteModel;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

import java.util.List;

/**
 * Created by lichenghang on 2019/1/29.
 */

public class GetCloudNotesCase extends UseCase<RemoteNoteSource.NetNoteQuery, List<NoteModel>> {

    private RemoteNoteSource remoteNoteSource;

    public GetCloudNotesCase(RemoteNoteSource remoteNoteSource) {
        this.remoteNoteSource = remoteNoteSource;
    }

    @Override
    protected ResponseValue<List<NoteModel>> execute(RemoteNoteSource.NetNoteQuery parameters) {
        return remoteNoteSource.queryNotes(parameters);
    }
}
