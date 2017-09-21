package com.lch.menote.home.route

import com.lch.menote.common.launchActivity
import com.lch.menote.home.HomeActivity
import com.lch.route.noaop.lib.RouteEngine
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService("home")
class HomeModule {

    @RouteMethod("home")
    fun launchHome(params: Map<String, String>) {
        RouteEngine.context.launchActivity(HomeActivity::class.java)
    }

}