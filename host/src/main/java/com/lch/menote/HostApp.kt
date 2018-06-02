package com.lch.menote

import android.app.Application
import com.bilibili.boxing.BoxingMediaLoader
import com.blankj.utilcode.util.Utils
import com.lch.menote.common.Glo
import com.lch.menote.common.util.ContextProvider
import com.lch.menote.home.route.HomeRouteApiImpl
import com.lch.menote.note.route.NoteRouteApiImpl
import com.lch.menote.share.ShareTool
import com.lch.menote.user.route.UserRouteApiImpl
import com.lch.netkit.NetKit
import com.lch.route.noaop.lib.RouteEngine


/**
 * Created by Administrator on 2017/9/21.
 */
class HostApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { _, e -> e.printStackTrace() }
        Glo.init(this)
        NetKit.init()

        ContextProvider.initContext(this)
        Utils.init(this)
        BoxingMediaLoader.getInstance().init( IBoxingMediaLoaderImpl()) // a class implements IBoxingMediaLoader
       // BoxingCrop.getInstance().init(BoxingUcrop())
        ShareTool.initSdk(this)

        RouteEngine.init(this, HomeRouteApiImpl::class.java, NoteRouteApiImpl::class.java, UserRouteApiImpl::class.java)

    }
}