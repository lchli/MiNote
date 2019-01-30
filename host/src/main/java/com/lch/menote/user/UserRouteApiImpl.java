package com.lch.menote.user;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lch.menote.user.dataimpl.MemPwdSource;
import com.lch.menote.user.route.User;
import com.lch.menote.user.ui.LockPwdActivity;
import com.lch.menote.user.ui.UserFragmentContainer;
import com.lchli.utils.tool.Navigator;

/**
 * Created by lichenghang on 2018/5/19.
 */
 public class UserRouteApiImpl implements UserApi {

    public static final String SP = "user-sp";


    private MemPwdSource mMemUserRepo = new MemPwdSource();


    @Override
    public Fragment indexPage() {
        return new UserFragmentContainer();
    }

    @Override
    public void lockPwdPage(Context context) {

        Navigator.launchActivity(context, LockPwdActivity.class);
    }

    @Override
    public String getLockPwd() {

        return mMemUserRepo.getLockPwd().data;
    }

    @Override
    public void onAppBackground() {
        mMemUserRepo.clearLockPwd();
    }

    @Override
    public User getSession() {
        return UserModuleInjector.getINS().provideLocalUserDataSource().getUser().data;
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
