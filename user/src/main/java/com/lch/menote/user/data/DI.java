package com.lch.menote.user.data;

import com.lch.menote.user.data.mem.UserMemSource;
import com.lch.menote.user.data.net.UserNetSource;
import com.lch.menote.user.data.sp.UserSpSource;

/**
 * Created by lichenghang on 2018/5/19.
 */

public final class DI {

    public static UserSource provideNetSource(){
        return  UserNetSource.INSTANCE;
    }

    public static UserSource provideSpSource(){
        return  UserSpSource.INSTANCE;
    }

    public static UserSource provideMemSource(){
        return  UserMemSource.INSTANCE;
    }
}
