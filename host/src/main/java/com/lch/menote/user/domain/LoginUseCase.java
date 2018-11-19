package com.lch.menote.user.domain;

import com.lch.menote.UseCase;
import com.lch.menote.user.data.RemoteUserDataSource;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class LoginUseCase extends UseCase<LoginUseCase.LoginParams, User> {

    public static class LoginParams {

        public String userName;
        public String userPwd;
    }


    private RemoteUserDataSource dataSource;

    public LoginUseCase(RemoteUserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected ResponseValue<User> execute(LoginParams parameters) {
        return dataSource.getUser(parameters.userName, parameters.userPwd);
    }
}
