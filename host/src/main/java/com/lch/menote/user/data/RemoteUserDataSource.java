package com.lch.menote.user.data;

import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public interface RemoteUserDataSource {

    ResponseValue<User> addUser(String userName, String userPwd, String userHeadUrl);
    ResponseValue<User> getUser(String userName, String userPwd);
    ResponseValue<User> updateUser(User user);
    ResponseValue<User> getUser(String userId);
}
