package com.lch.menote.user.data.sp;

import com.blankj.utilcode.util.SPUtils;
import com.lch.menote.common.util.AliJsonHelper;
import com.lch.menote.user.route.UserRouteApiImpl;
import com.lch.menote.userapi.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class SpUserRepo {
    private static final String KEY_USER_SESSION = "KEY_USER_SESSION";

    public ResponseValue<User> getUser() {
        ResponseValue<User> res = new ResponseValue<>();

        try {
            String json = SPUtils.getInstance(UserRouteApiImpl.SP).getString(KEY_USER_SESSION);
            res.data = AliJsonHelper.parseObject(json, User.class);
        } catch (Throwable e) {
            e.printStackTrace();
            res.setErrMsg(e.getMessage());
        }

        return res;
    }

    public ResponseValue<Void> saveUser(User user) {
        SPUtils.getInstance(UserRouteApiImpl.SP).put(KEY_USER_SESSION, AliJsonHelper.toJSONString(user));
        return new ResponseValue<>();
    }

    public ResponseValue<Void> removeCurrentUser() {
        SPUtils.getInstance(UserRouteApiImpl.SP).remove(KEY_USER_SESSION);
        return new ResponseValue<>();
    }
}
