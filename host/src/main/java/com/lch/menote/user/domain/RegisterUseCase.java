package com.lch.menote.user.domain;

import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.mvc.UseCase;

public class RegisterUseCase extends UseCase<RegisterUseCase.RegisterParams, User> {

    public static class RegisterParams {

        public String userName;
        public String userPwd;
        public String userHeadUrl;
    }


    private RemoteUserDataSource userDataSource;

    public RegisterUseCase(RemoteUserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    @Override
    protected ResponseValue<User> execute(RegisterParams parameters) {
        return userDataSource.addUser(parameters.userName, parameters.userPwd, parameters.userHeadUrl);
    }
}
