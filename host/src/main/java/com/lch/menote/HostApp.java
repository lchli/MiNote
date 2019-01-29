package com.lch.menote;

import android.app.Application;

import com.bilibili.boxing.BoxingMediaLoader;
import com.blankj.utilcode.util.Utils;
import com.lch.menote.file.FileModuleFactory;
import com.lch.menote.file.FileModuleInjector;
import com.lch.menote.file.dataImpl.NetFileSource;
import com.lch.menote.file.datainterface.RemoteFileSource;
import com.lch.menote.user.UserApiImpl;
import com.lch.menote.user.UserApiManager;
import com.lch.menote.user.UserModuleFactory;
import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.dataimpl.MemPwdSource;
import com.lch.menote.user.dataimpl.NetAppUpdateInfoSource;
import com.lch.menote.user.dataimpl.NetUserDataSource;
import com.lch.menote.user.dataimpl.SpUserDataSource;
import com.lch.menote.user.datainterface.AppUpdateInfoDataSource;
import com.lch.menote.user.datainterface.PwdSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.netkit.v2.NetKit;
import com.lchli.imgloader.ImgLoaderManager;
import com.lchli.utils.tool.ContextProvider;


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

        // RouteEngine.INSTANCE.init(this, HomeRouteApiImpl.class, NoteRouteApiImpl.class, UserRouteApiImpl.class);

        ImgLoaderManager.getINS().init(this, null);


        UserModuleInjector.getINS().initModuleFactory(new UserModuleFactory() {
            @Override
            public UserSessionDataSource provideLocalUserDataSource() {
                return new SpUserDataSource();
            }

            @Override
            public RemoteUserDataSource provideRemoteUserDataSource() {
                return new NetUserDataSource();
            }

            @Override
            public AppUpdateInfoDataSource provideAppUpdateInfoDataSource() {
                return new NetAppUpdateInfoSource();
            }

            @Override
            public PwdSource providePwdSource() {
                return new MemPwdSource();
            }
        });
        UserApiManager.getINS().initImpl(new UserApiImpl());


        FileModuleInjector.getINS().initModuleFactory(new FileModuleFactory() {
            @Override
            public RemoteFileSource provideRemoteFileSource() {
                return new NetFileSource();
            }
        });


    }

}