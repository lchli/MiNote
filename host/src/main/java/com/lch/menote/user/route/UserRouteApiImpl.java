package com.lch.menote.user.route;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lch.menote.user.LockPwdActivity;
import com.lch.menote.user.data.mem.MemUserRepo;
import com.lch.menote.user.data.sp.SpUserRepo;
import com.lch.menote.user.ui.UserFragmentContainer;
import com.lch.netkit.common.tool.Navigator;
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
    private MemUserRepo mMemUserRepo = new MemUserRepo();
    private SpUserRepo mSpUserRepo = new SpUserRepo();

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

        return mMemUserRepo.getLockPwd().data;
    }

    @Override
    public void onAppBackground(Map<String, String> params) {
        mMemUserRepo.clearLockPwd();
    }

    @Override
    public User userSession() {
        return mSpUserRepo.getUser().data;
    }

    @Override
    public User queryUser(String userId) {
        try {
            return null;//DI.provideNetSource().getUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
