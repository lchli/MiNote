package com.lch.menote.user.domain;

import com.lch.menote.UseCase;
import com.lch.menote.user.data.UserRepo;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class LoginUseCase extends UseCase<LoginUseCase.LoginParams, User> {

    public static class LoginParams {

        public String userName;
        public String userPwd;
    }


    private UserRepo userRepo;

    public LoginUseCase(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    protected ResponseValue<User> execute(LoginParams parameters) {
        return userRepo.login(parameters.userName, parameters.userPwd);
    }
}
