package com.lch.menote.home.route;

import android.support.annotation.Nullable;

import com.lch.menote.noteapi.NoteRouteApi;
import com.lch.menote.userapi.UserRouteApi;
import com.lch.route.noaop.lib.RouteEngine;

/**
 * Created by lichenghang on 2018/5/19.
 */

public final class RouteCall {

    @Nullable
    public static UserRouteApi getUserModule(){

       return (UserRouteApi) RouteEngine.INSTANCE.getModule(UserRouteApi.MODULE_NAME);
    }

    @Nullable
    public static NoteRouteApi getNoteModule(){

        return (NoteRouteApi) RouteEngine.INSTANCE.getModule(NoteRouteApi.MODULE_NAME);
    }
}
