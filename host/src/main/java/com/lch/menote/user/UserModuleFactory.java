package com.lch.menote.user;

import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;

/**
 * Created by Administrator on 2018/12/26.
 */

public interface UserModuleFactory {

    UserSessionDataSource provideLocalUserDataSource();

    RemoteUserDataSource provideRemoteUserDataSource();
}
