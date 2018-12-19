package com.lch.menote.user.dataimpl;

import com.lch.menote.user.datainterface.LocalUserDataSource;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class SpUserDataSource implements LocalUserDataSource {
    @Override
    public ResponseValue<User> updateUser(User user) {
        return null;
    }

    @Override
    public ResponseValue<User> getUser() {
        return null;
    }
}
