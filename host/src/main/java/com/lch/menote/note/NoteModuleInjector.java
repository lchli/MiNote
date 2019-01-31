package com.lch.menote.note;

import com.lch.menote.note.datainterface.LocalNoteSource;
import com.lch.menote.note.datainterface.NoteElementSource;
import com.lch.menote.note.datainterface.NoteTagSource;
import com.lch.menote.note.datainterface.RemoteNoteSource;

/**
 * Created by Administrator on 2018/12/26.
 */

public final class NoteModuleInjector implements NoteModuleFactory {

    private static final NoteModuleInjector INS = new NoteModuleInjector();

    public static NoteModuleInjector getINS() {
        return INS;
    }

    private NoteModuleFactory moduleFactory;

    public void initModuleFactory(NoteModuleFactory factory) {
        moduleFactory = factory;
    }

    @Override
    public LocalNoteSource provideLocalNoteSource() {
        return moduleFactory.provideLocalNoteSource();
    }

    @Override
    public NoteTagSource provideNoteTagSource() {
        return moduleFactory.provideNoteTagSource();
    }

    @Override
    public RemoteNoteSource provideRemoteNoteSource() {
        return moduleFactory.provideRemoteNoteSource();
    }

    @Override
    public NoteElementSource provideNoteElementSource() {
        return moduleFactory.provideNoteElementSource();
    }
}
