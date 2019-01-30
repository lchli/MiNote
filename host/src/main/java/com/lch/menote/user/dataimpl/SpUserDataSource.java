package com.lch.menote.user.dataimpl;

import com.blankj.utilcode.util.SPUtils;
import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.route.User;
import com.lch.menote.user.UserRouteApiImpl;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.utils.tool.AliJsonHelper;


public class SpUserDataSource implements UserSessionDataSource {
    private static final String KEY_USER_SESSION = "KEY_USER_SESSION";

    @Override
    public void saveUser(User user) {
        SPUtils.getInstance(UserRouteApiImpl.SP).put(KEY_USER_SESSION, AliJsonHelper.toJSONString(user));
    }

    @Override
    public ResponseValue<User> getUser() {
        ResponseValue<User> res = new ResponseValue<>();

        try {
            String json = SPUtils.getInstance(UserRouteApiImpl.SP).getString(KEY_USER_SESSION);
            res.data = AliJsonHelper.parseObject(json, User.class);
        } catch (Throwable e) {
            e.printStackTrace();
            res.setErrorMsg(e.getMessage());
        }

        return res;
    }
}
