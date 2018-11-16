package com.lch.menote.user.data;

import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class UserRepo {

    private RemoteUserDataSource userDataSource;

    public UserRepo(RemoteUserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    public ResponseValue<User> register(String userName, String userPwd, String userHeadUrl) {
        return userDataSource.addUser(userName, userPwd, userHeadUrl);
    }

    public ResponseValue<User> login(String userName, String userPwd) {
        return userDataSource.getUser(userName, userPwd);
    }

    public ResponseValue<User> updateUser(User user){
        return userDataSource.updateUser(user);
    }

    public ResponseValue<User> getUser(String userId){
        return userDataSource.getUser(userId);
    }
}
