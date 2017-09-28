package com.lch.menote.common.route

import android.support.v4.app.Fragment

/**
 * Created by Administrator on 2017/9/28.
 */
interface UserMod {

    companion object {
        const val MODULE_NAME = "user"
        const val INDEX = "index"
        const val PWD_PAGE = "lockPwdPage"
        const val GET_LOCK_PWD = "getLockPwd"
        const val ROUTE_PATH_GET_LOCK_PWD = "$MODULE_NAME/$GET_LOCK_PWD"
    }


    fun indexPage(params: Map<String, String>? = null): Fragment

    fun lockPwdPage(params: Map<String, String>? = null)

    fun getLockPwd(params: Map<String, String>? = null): String

    fun onAppBackground(params: Map<String, String>? = null)
}