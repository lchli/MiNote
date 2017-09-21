package com.lch.menote.user

import android.support.v4.app.Fragment
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService("user")
object UserModule {

    @RouteMethod("indexPage")
    fun indexPage(params: Map<String, String>): Fragment {
        return UserFragment()
    }
}