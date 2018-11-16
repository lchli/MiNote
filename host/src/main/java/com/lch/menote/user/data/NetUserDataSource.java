package com.lch.menote.user.data;

import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class NetUserDataSource implements RemoteUserDataSource {

    @Override
    public ResponseValue<User> addUser(String userName, String userPwd, String userHeadUrl) {
        return null;
    }

    @Override
    public ResponseValue<User> getUser(String userName, String userPwd) {
        return null;
    }

    @Override
    public ResponseValue<User> updateUser(User user) {
        return null;
    }

    @Override
    public ResponseValue<User> getUser(String userId) {
        return null;
    }
}
