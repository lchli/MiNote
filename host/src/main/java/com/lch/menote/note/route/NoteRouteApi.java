package com.lch.menote.note.route;

import android.support.v4.app.Fragment;

import java.util.Map;

/**
 * Created by lichenghang on 2018/5/19.
 */

public interface NoteRouteApi {

    String MODULE_NAME = "note";

    Fragment localFrament(Map<String, String> params);

    Fragment cloudFragment(Map<String, String> params);

    void clearDB(Map<String, String> params);

    void onAppBackground(Map<String, String> params);

    void onUserLogout(Map<String, String> params);

    void onUserLogin(Map<String, String> params);

}
