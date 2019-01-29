package com.lch.menote.user;

import com.lch.menote.user.datainterface.AppUpdateInfoDataSource;
import com.lch.menote.user.datainterface.PwdSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.datainterface.UserSessionDataSource;

/**
 * Created by Administrator on 2018/12/26.
 */

public interface UserModuleFactory {

    UserSessionDataSource provideLocalUserDataSource();

    RemoteUserDataSource provideRemoteUserDataSource();

    AppUpdateInfoDataSource provideAppUpdateInfoDataSource();

    PwdSource providePwdSource();
}
