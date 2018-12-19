package com.lch.menote.user.datainterface;

import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public interface LocalUserDataSource {

    ResponseValue<User> updateUser(User user);

    ResponseValue<User> getUser();
}
