package com.lch.menote.user.datainterface;

import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public interface RemoteUserDataSource {

    ResponseValue<User> addUser(String userName, String userPwd, String userHeadUrl);

    ResponseValue<User> getUser(String userName, String userPwd);

    ResponseValue<User> updateUser(String updateUserId, String sessionUid, String sessionToken, UpdateUserParams updateUserParams);

    ResponseValue<User> getUser(String userId, String sessionUid, String sessionToken);

    public static class UpdateUserParams {
        public String name;
        public String pwd;
        public String headUrl;
        public String userContact;
    }
}
