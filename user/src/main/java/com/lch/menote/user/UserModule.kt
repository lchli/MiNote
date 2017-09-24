package com.lch.menote.user

import android.content.Context
import android.support.v4.app.Fragment
import com.lch.menote.common.route.UserModulePaths
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService
import com.lch.route.noaop.lib.Router

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService(UserModulePaths.MODULE_NAME)
class UserModule : Router {

    override fun init(context: Context) {

    }

    @RouteMethod(UserModulePaths.INDEX)
    fun indexPage(params: Map<String, String>): Fragment {
        return UserFragment()
    }
}