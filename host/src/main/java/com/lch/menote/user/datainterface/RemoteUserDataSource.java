package com.lch.menote.user.datainterface;

import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ResponseValue;

public interface RemoteUserDataSource {

    ResponseValue<User> addUser(String userName, String userPwd, String userHeadUrl,String userContact);

    ResponseValue<User> getUser(String userName, String userPwd);

    ResponseValue<User> updateUser(UpdateUserParams updateUserParams);

    ResponseValue<User> getUser(String userId);

      class UpdateUserParams {
        public String pwd;
        public String headUrl;
        public String userContact;
    }
}
