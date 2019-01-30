package com.lch.menote.note;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2019/1/29.
 * called after module inited.
 */

public final class NoteApiManager implements NoteApi {
    private NoteApi impl;

    private static final NoteApiManager INS = new NoteApiManager();

    public static NoteApiManager getINS() {
        return INS;
    }

    public void initImpl(NoteApi impl) {
        this.impl = impl;
    }

    @Override
    public Fragment localFrament() {
        return null;
    }

    @Override
    public Fragment cloudFragment() {
        return null;
    }

    @Override
    public void clearDB() {

    }

    @Override
    public void onAppBackground() {

    }

    @Override
    public void onUserLogout() {

    }

    @Override
    public void onUserLogin() {

    }
}
