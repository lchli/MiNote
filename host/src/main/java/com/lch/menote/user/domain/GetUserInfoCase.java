package com.lch.menote.user.domain;

import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * 传给用例的一定是数据源的抽象接口。model转换器？如果从数据源拿到的是entity应该使用转换器转换为model
 */
public class GetUserInfoCase extends UseCase<GetUserInfoCase.Params, User> {


    private final RemoteUserDataSource dataSource;


    public static class Params {

        public String userId;
        public String sessionUid;
        public String sessionToken;
    }


    public GetUserInfoCase(RemoteUserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected ResponseValue<User> execute(Params parameters) {
        return dataSource.getUser(parameters.userId, parameters.sessionUid, parameters.sessionToken);
    }
}
