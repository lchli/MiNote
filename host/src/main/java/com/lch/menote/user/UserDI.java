package com.lch.menote.user;

import com.lch.menote.file.data.NetFileSource;
import com.lch.menote.user.data.NetUserDataSource;
import com.lch.menote.user.data.SpUserDataSource;
import com.lch.menote.user.presenterx.UserPresenter;

public final class UserDI {


    public static UserPresenter provideUserPresenter() {
        return new UserPresenter(new SpUserDataSource(), new NetUserDataSource(), new NetFileSource());
    }
}
