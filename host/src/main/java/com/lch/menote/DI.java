package com.lch.menote;

import com.lch.menote.user.data.LocalUserDataSource;
import com.lch.menote.user.data.NetUserDataSource;
import com.lch.menote.user.data.RemoteUserDataSource;

public final class DI {


    public static RemoteUserDataSource provideRemoteUserDataSource() {
        return new NetUserDataSource();
    }


    public static RemoteUserDataSource provideLocalUserDataSource() {
        return new LocalUserDataSource();
    }
}
