package com.lch.menote

import android.app.Application
import com.bilibili.boxing.BoxingMediaLoader
import com.blankj.utilcode.util.Utils
import com.lch.menote.home.route.HomeRouteApiImpl
import com.lch.menote.note.route.NoteRouteApiImpl
import com.lch.menote.user.route.UserRouteApiImpl
import com.lch.netkit.NetKit
import com.lch.netkit.common.tool.ContextProvider
import com.lch.route.noaop.lib.RouteEngine


/**
 * Created by Administrator on 2017/9/21.
 */
class HostApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { _, e -> e.printStackTrace() }
        NetKit.init(this)

        ContextProvider.initContext(this)
        Utils.init(this)
        BoxingMediaLoader.getInstance().init( IBoxingMediaLoaderImpl()) // a class implements IBoxingMediaLoader
       // BoxingCrop.getInstance().init(BoxingUcrop())

        RouteEngine.init(this, HomeRouteApiImpl::class.java, NoteRouteApiImpl::class.java, UserRouteApiImpl::class.java)

    }
}