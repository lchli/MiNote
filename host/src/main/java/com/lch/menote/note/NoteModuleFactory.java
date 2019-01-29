package com.lch.menote.note;

import com.lch.menote.note.datainterface.LocalNoteSource;
import com.lch.menote.note.datainterface.NoteTagSource;
import com.lch.menote.note.datainterface.RemoteNoteSource;

/**
 * Created by Administrator on 2018/12/26.
 */

public interface NoteModuleFactory {

    LocalNoteSource provideLocalNoteSource();
    NoteTagSource provideNoteTagSource();
    RemoteNoteSource provideRemoteNoteSource();
}
