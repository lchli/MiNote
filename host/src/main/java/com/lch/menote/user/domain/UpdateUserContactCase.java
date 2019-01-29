package com.lch.menote.user.domain;

import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;


public class UpdateUserContactCase extends UseCase<UpdateUserContactCase.UpdateParams, User> {

    private final UserSessionDataSource sessionDataSource;

    public static class UpdateParams {
        public String userContact;
    }


    private RemoteUserDataSource dataSource;

    public UpdateUserContactCase(RemoteUserDataSource dataSource, UserSessionDataSource sessionDataSource) {
        this.dataSource = dataSource;
        this.sessionDataSource = sessionDataSource;
    }

    @Override
    protected ResponseValue<User> execute(UpdateParams parameters) {
        ResponseValue<User> res = sessionDataSource.getUser();
        if (res.hasError()) {
            return res;
        }
        if (res.data == null) {
            res.setErrorMsg("session is null.");
            return res;
        }

        RemoteUserDataSource.UpdateUserParams updateUserParams = new RemoteUserDataSource.UpdateUserParams();
        updateUserParams.userContact = parameters.userContact;
        updateUserParams.headUrl = res.data.headUrl;
        updateUserParams.name = res.data.name;
        updateUserParams.pwd = res.data.pwd;

        return dataSource.updateUser(res.data.uid, res.data.uid, res.data.token, updateUserParams);
    }
}
