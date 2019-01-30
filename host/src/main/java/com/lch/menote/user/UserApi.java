package com.lch.menote.user;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lch.menote.user.route.User;

/**
 * Created by Administrator on 2019/1/29.
 */

public interface UserApi {

    User getSession();

    Fragment indexPage();

    void lockPwdPage(Context context);

    String getLockPwd();

    void onAppBackground();

    User queryUser(String userId);

}
