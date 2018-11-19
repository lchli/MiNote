package com.lch.menote.user.domain;

import com.lch.menote.UseCase;
import com.lch.menote.user.data.LocalUserDataSource;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class SaveUserSessionUseCase extends UseCase<SaveUserSessionUseCase.Params, Void> {

    public static class Params {

        public User session;
    }


    private LocalUserDataSource userDataSource;

    public SaveUserSessionUseCase(LocalUserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    @Override
    protected ResponseValue<Void> execute(Params parameters) {
        ResponseValue<User> res = userDataSource.updateUser(parameters.session);

        ResponseValue<Void> ret = new ResponseValue<>();
        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
        }

        return ret;
    }
}
