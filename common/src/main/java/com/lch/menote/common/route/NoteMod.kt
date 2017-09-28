package com.lch.menote.common.route

import android.support.v4.app.Fragment

/**
 * Created by Administrator on 2017/9/28.
 */
interface NoteMod {

    companion object {
        const val MODULE_NAME = "note"
        const val LOCAL_NOTE = "local"
        const val ROUTE_PATH_LOCAL_NOTE = "$MODULE_NAME/$LOCAL_NOTE"
        const val CLOUD_NOTE = "cloud"
        const val ROUTE_PATH_CLOUD_NOTE = "$MODULE_NAME/$CLOUD_NOTE"
    }


    fun localFrament(params: Map<String, String>? = null): Fragment

    fun cloudFragment(params: Map<String, String>? = null): Fragment

    fun clearDB(params: Map<String, String>? = null)

    fun onAppBackground(params: Map<String, String>? = null)

}