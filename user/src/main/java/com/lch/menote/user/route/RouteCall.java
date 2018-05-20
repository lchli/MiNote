package com.lch.menote.user.route;

import android.support.annotation.Nullable;

import com.lch.menote.homeapi.HomeRouteApi;
import com.lch.menote.noteapi.NoteRouteApi;
import com.lch.route.noaop.lib.RouteEngine;

/**
 * Created by lichenghang on 2018/5/19.
 */

public final class RouteCall {

    @Nullable
    public static NoteRouteApi getNoteModule(){

       return (NoteRouteApi) RouteEngine.INSTANCE.getModule(NoteRouteApi.MODULE_NAME);
    }

    @Nullable
    public static HomeRouteApi getHomeModule(){

        return (HomeRouteApi) RouteEngine.INSTANCE.getModule(HomeRouteApi.MODULE_NAME);
    }
}
