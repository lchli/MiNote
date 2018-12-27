package com.lch.menote.user;

import com.lch.menote.user.datainterface.AppUpdateInfoDataSource;
import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;

/**
 * Created by Administrator on 2018/12/26.
 */

public final class UserModuleInjector implements UserModuleFactory {

    private static final UserModuleInjector INS = new UserModuleInjector();

    public static UserModuleInjector getINS() {
        return INS;
    }

    private UserModuleFactory moduleFactory;

    public void initModuleFactory(UserModuleFactory factory) {
        moduleFactory = factory;
    }

    @Override
    public UserSessionDataSource provideLocalUserDataSource() {
        return moduleFactory.provideLocalUserDataSource();
    }

    @Override
    public RemoteUserDataSource provideRemoteUserDataSource() {
        return moduleFactory.provideRemoteUserDataSource();
    }

    @Override
    public AppUpdateInfoDataSource provideAppUpdateInfoDataSource() {
        return moduleFactory.provideAppUpdateInfoDataSource();
    }
}
