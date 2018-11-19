package com.lch.menote.user.domain;

import com.lch.menote.UseCase;
import com.lch.menote.user.data.RemoteUserDataSource;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class UpdateUseCase extends UseCase<UpdateUseCase.UpdateParams, User> {

    public static class UpdateParams {

        public User user;
    }


    private RemoteUserDataSource dataSource;

    public UpdateUseCase(RemoteUserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected ResponseValue<User> execute(UpdateParams parameters) {
        return dataSource.updateUser(parameters.user);
    }
}
