package com.lch.menote.user.route;


import android.support.v4.app.Fragment;

import java.util.Map;

/**
 * Created by lichenghang on 2018/5/19.
 */

public interface UserRouteApi {

    String MODULE_NAME = "user";
    String INDEX = "index";
    String PWD_PAGE = "lockPwdPage";
    String GET_LOCK_PWD = "getLockPwd";
    String ROUTE_PATH_GET_LOCK_PWD = "$MODULE_NAME/$GET_LOCK_PWD";


    Fragment indexPage(Map<String, String> params);

    void lockPwdPage(Map<String, String> params);

    String getLockPwd(Map<String, String> params);

    void onAppBackground(Map<String, String> params);

    User userSession();

    User queryUser(String userId);
}
