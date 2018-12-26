package com.lch.menote.user.datainterface;

import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public interface UserSessionDataSource {

    void saveUser(User user);

    ResponseValue<User> getUser();
}
