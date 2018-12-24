package com.lch.menote.user.domain;

import com.lch.menote.user.datainterface.LocalUserDataSource;
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
    private final LocalUserDataSource localUserDataSource;

    public RegisterUseCase(RemoteUserDataSource userDataSource, LocalUserDataSource localUserDataSource) {
        this.userDataSource = userDataSource;
        this.localUserDataSource = localUserDataSource;
    }

    @Override
    protected ResponseValue<User> execute(RegisterParams parameters) {
        ResponseValue<User> res = userDataSource.addUser(parameters.userName, parameters.userPwd, parameters.userHeadUrl);
        if (res.hasError() || res.data == null) {
            return res;
        }
        return localUserDataSource.updateUser(res.data);
    }
}
