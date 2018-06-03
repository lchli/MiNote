package com.lch.menote.app;

import android.support.annotation.Nullable;

import com.lch.menote.user.route.UserRouteApi;
import com.lch.route.noaop.lib.RouteEngine;

/**
 * Created by lichenghang on 2018/5/19.
 */

public final class RouteCall {

    @Nullable
    public static UserRouteApi getUserModule(){

       return (UserRouteApi) RouteEngine.INSTANCE.getModule(UserRouteApi.MODULE_NAME);
    }
}
