package com.lch.menote.user

import android.content.Context
import android.support.v4.app.Fragment
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService
import com.lch.route.noaop.lib.Router

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService("user")
class UserModule : Router {

    override fun init(context: Context) {

    }

    @RouteMethod("indexPage")
    fun indexPage(params: Map<String, String>): Fragment {
        return UserFragment()
    }
}