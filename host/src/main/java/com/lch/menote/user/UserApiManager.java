package com.lch.menote.user;

import android.content.Context;
import android.support.v4.app.Fragment;

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

    @Override
    public Fragment indexPage() {
        return userApi.indexPage();
    }

    @Override
    public void lockPwdPage(Context context) {
        userApi.lockPwdPage(context);
    }

    @Override
    public String getLockPwd() {
        return userApi.getLockPwd();
    }

    @Override
    public void onAppBackground() {
        userApi.onAppBackground();
    }

    @Override
    public User queryUser(String userId) {
        return userApi.queryUser(userId);
    }
}
