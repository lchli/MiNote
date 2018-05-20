package com.lch.menote.user.route;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lch.menote.common.util.Navigator;
import com.lch.menote.user.LockPwdActivity;
import com.lch.menote.user.data.DI;
import com.lch.menote.user.ui.UserFragmentContainer;
import com.lch.menote.userapi.User;
import com.lch.menote.userapi.UserRouteApi;
import com.lch.route.noaop.lib.RouteMethod;
import com.lch.route.noaop.lib.RouteService;
import com.lch.route.noaop.lib.Router;

import java.util.Map;

/**
 * Created by lichenghang on 2018/5/19.
 */
@RouteService(UserRouteApi.MODULE_NAME)
 public class UserRouteApiImpl implements UserRouteApi, Router {

    public static final String SP = "user-sp";

    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    @RouteMethod(UserRouteApi.INDEX)
    @Override
    public Fragment indexPage(Map<String, String> params) {
        return new UserFragmentContainer();
    }

    @RouteMethod(UserRouteApi.PWD_PAGE)
    @Override
    public void lockPwdPage(Map<String, String> params) {

        Navigator.launchActivity(mContext, LockPwdActivity.class);
    }

    @RouteMethod(UserRouteApi.GET_LOCK_PWD)
    @Override
    public String getLockPwd(Map<String, String> params) {
        return DI.provideMemSource().getLockPwd();
    }

    @Override
    public void onAppBackground(Map<String, String> params) {
        DI.provideMemSource().clearLockPwd();
    }

    @Override
    public User userSession() {
        return DI.provideSpSource().getUser();
    }

    @Override
    public User queryUser(String userId) {
        try {
            return DI.provideNetSource().getUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
