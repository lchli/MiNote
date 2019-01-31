package com.lch.menote.note;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2019/1/30.
 */

public interface NoteApi {

    Fragment localFrament();

    void clearDB();

    void onAppBackground();

    void onUserLogout();

    void onUserLogin();

}
