package com.lch.menote.app;

import android.app.Application;

import com.bilibili.boxing.BoxingMediaLoader;
import com.blankj.utilcode.util.Utils;
import com.lch.menote.IBoxingMediaLoaderImpl;
import com.lch.menote.home.route.HomeRouteApiImpl;
import com.lch.menote.note.route.NoteRouteApiImpl;
import com.lch.menote.user.route.UserRouteApiImpl;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.tool.ContextProvider;
import com.lch.route.noaop.lib.RouteEngine;


/**
 * Created by Administrator on 2017/9/21.
 */
public class HostApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
        NetKit.init(this);

        ContextProvider.initContext(this);
        Utils.init(this);
        BoxingMediaLoader.getInstance().init(new IBoxingMediaLoaderImpl());

        RouteEngine.INSTANCE.init(this, HomeRouteApiImpl.class, NoteRouteApiImpl.class, UserRouteApiImpl.class);


    }

}