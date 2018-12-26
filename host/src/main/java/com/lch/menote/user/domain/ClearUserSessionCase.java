package com.lch.menote.user.domain;

import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.mvc.UseCase;

/**
 * 传给用例的一定是数据源的抽象接口。model转换器？如果从数据源拿到的是entity应该使用转换器转换为model
 */
public class ClearUserSessionCase extends UseCase<Void, Void> {

    private final UserSessionDataSource localUserDataSource;


    public ClearUserSessionCase(UserSessionDataSource localUserDataSource) {
        this.localUserDataSource = localUserDataSource;
    }

    @Override
    protected ResponseValue<Void> execute(Void parameters) {
        localUserDataSource.saveUser(null);
        return new ResponseValue<>();
    }
}
