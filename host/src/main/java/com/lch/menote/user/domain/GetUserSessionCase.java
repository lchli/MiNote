package com.lch.menote.user.domain;

import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.mvc.UseCase;

/**
 * 传给用例的一定是数据源的抽象接口。model转换器？如果从数据源拿到的是entity应该使用转换器转换为model
 */
public class GetUserSessionCase extends UseCase<Void, User> {

    private final UserSessionDataSource localUserDataSource;


    public GetUserSessionCase(UserSessionDataSource localUserDataSource) {
        this.localUserDataSource = localUserDataSource;
    }

    @Override
    protected ResponseValue<User> execute(Void parameters) {
        return localUserDataSource.getUser();
    }
}
