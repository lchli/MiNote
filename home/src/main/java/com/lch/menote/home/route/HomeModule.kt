package com.lch.menote.home.route

import android.content.Context
import com.lch.menote.common.launchActivity
import com.lch.menote.common.route.HomeModulePaths
import com.lch.menote.home.HomeActivity
import com.lch.route.noaop.lib.RouteEngine
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService
import com.lch.route.noaop.lib.Router

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService(HomeModulePaths.MODULE_NAME)
class HomeModule :Router{

    override fun init(context: Context) {

    }

    @RouteMethod(HomeModulePaths.HOME)
    fun launchHome(params: Map<String, String>) {
        RouteEngine.context.launchActivity(HomeActivity::class.java)
    }

}