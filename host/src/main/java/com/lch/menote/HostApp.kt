package com.lch.menote

import android.app.Application
import com.lch.menote.common.util.ContextProvider
import com.lch.menote.home.route.HomeModule
import com.lch.menote.note.route.NoteModule
import com.lch.menote.user.UserModule
import com.lch.route.noaop.lib.RouteEngine

/**
 * Created by Administrator on 2017/9/21.
 */
class HostApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { _, e -> e.printStackTrace() }

        ContextProvider.initContext(this)

        RouteEngine.init(this, HomeModule::class.java, NoteModule::class.java, UserModule::class.java)
    }
}