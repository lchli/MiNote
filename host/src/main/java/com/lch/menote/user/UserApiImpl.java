package com.lch.menote.user;

import com.lch.menote.user.route.User;

/**
 * Created by Administrator on 2019/1/29.
 */

public class UserApiImpl implements UserApi {

    @Override
    public User getSession() {
        return UserModuleInjector.getINS().provideLocalUserDataSource().getUser().data;
    }
}
