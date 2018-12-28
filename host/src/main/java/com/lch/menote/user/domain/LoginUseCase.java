package com.lch.menote.user.domain;

import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * 传给用例的一定是数据源的抽象接口。model转换器？如果从数据源拿到的是entity应该使用转换器转换为model
 */
public class LoginUseCase extends UseCase<LoginUseCase.LoginParams, User> {

    private final UserSessionDataSource localUserDataSource;
    private RemoteUserDataSource dataSource;


    public static class LoginParams {

        public String userName;
        public String userPwd;
    }


    public LoginUseCase(RemoteUserDataSource dataSource, UserSessionDataSource localUserDataSource) {
        this.dataSource = dataSource;
        this.localUserDataSource = localUserDataSource;
    }

    @Override
    protected ResponseValue<User> execute(LoginParams parameters) {
        ResponseValue<User> res = dataSource.getUser(parameters.userName, parameters.userPwd);
        if (res.hasError() || res.data == null) {
            return res;
        }
        localUserDataSource.saveUser(res.data);

        return res;
    }
}
