package com.lch.menote

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lch.menote.common.route.HomeModulePaths
import com.lch.route.noaop.lib.RouteEngine

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RouteEngine.route(HomeModulePaths.ROUTE_PATH_HOME)

        finish()
    }
}
