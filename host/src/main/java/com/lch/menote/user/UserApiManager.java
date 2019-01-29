package com.lch.menote.user;

import com.lch.menote.user.route.User;

/**
 * Created by Administrator on 2019/1/29.
 * called after module inited.
 */

public final class UserApiManager implements UserApi {
    private UserApi userApi;

    private static final UserApiManager INS = new UserApiManager();

    public static UserApiManager getINS() {
        return INS;
    }

    public void initImpl(UserApi impl) {
        userApi = impl;
    }


    @Override
    public User getSession() {
        return userApi.getSession();
    }


}
