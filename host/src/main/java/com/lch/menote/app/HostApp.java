package com.lch.menote.app;

import android.app.Application;

import com.bilibili.boxing.BoxingMediaLoader;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lch.menote.IBoxingMediaLoaderImpl;
import com.lch.menote.home.route.HomeRouteApiImpl;
import com.lch.menote.note.route.NoteRouteApiImpl;
import com.lch.menote.user.route.UserRouteApiImpl;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.tool.ContextProvider;
import com.lch.route.noaop.lib.RouteEngine;
import com.lchli.litehotfix.ApplicationLike;


/**
 * Created by Administrator on 2017/9/21.
 */
public class HostApp extends ApplicationLike {

    public HostApp(Application application) {
        super(application);
    }



    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
        NetKit.init(getApplication());

        ContextProvider.initContext(getApplication());
        Utils.init(getApplication());
        BoxingMediaLoader.getInstance().init(new IBoxingMediaLoaderImpl());

        RouteEngine.INSTANCE.init(getApplication(), HomeRouteApiImpl.class, NoteRouteApiImpl.class, UserRouteApiImpl.class);


    }

}