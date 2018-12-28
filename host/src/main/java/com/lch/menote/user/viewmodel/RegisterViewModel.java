package com.lch.menote.user.viewmodel;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lch.menote.file.FileModuleInjector;
import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.domain.RegisterUseCase;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ControllerCallback;

import androidx.lifecycle.MutableLiveData;

/**
 * Created by Administrator on 2018/12/25.
 */

public class RegisterViewModel {
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> failMsg = new MutableLiveData<>();
    public MutableLiveData<Void> successEvent = new MutableLiveData<>();
    private final RegisterUseCase mRegisterUseCase = new RegisterUseCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource(), FileModuleInjector.getINS().provideRemoteFileSource());


    public void onRegister(String userName, String userPwd, String headPath) {
        loading.postValue(true);

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            failMsg.postValue("empty.");
            loading.postValue(false);
            return;
        }

        RegisterUseCase.RegisterParams p = new RegisterUseCase.RegisterParams();
        p.userName = userName;
        p.userPwd = userPwd;
        p.userHeadPath = headPath;

        mRegisterUseCase.invokeAsync(p, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                loading.postValue(false);

                if (user == null) {
                    failMsg.postValue("user is null.");
                    return;
                }

                successEvent.postValue(null);
            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });

    }
}
