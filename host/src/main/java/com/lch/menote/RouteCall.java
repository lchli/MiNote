package com.lch.menote;

import android.support.annotation.Nullable;

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
}
