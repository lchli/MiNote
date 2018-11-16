package com.lch.menote.user.domain;

import com.lch.menote.UseCase;
import com.lch.menote.user.data.UserRepo;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;

public class UpdateUseCase extends UseCase<UpdateUseCase.UpdateParams, User> {

    public static class UpdateParams {

        public User user;
    }


    private UserRepo userRepo;

    public UpdateUseCase(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    protected ResponseValue<User> execute(UpdateParams parameters) {
        return userRepo.updateUser(parameters.user);
    }
}
