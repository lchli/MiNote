package com.lch.menote.user.viewmodel;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lch.menote.user.datainterface.LocalUserDataSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.domain.LoginUseCase;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ControllerCallback;

import androidx.lifecycle.MutableLiveData;

/**
 * Created by Administrator on 2018/12/25.
 */

public class LoginViewModel {
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> failMsg = new MutableLiveData<>();
    public MutableLiveData<Void> successEvent = new MutableLiveData<>();
    private final LoginUseCase mLoginUseCase;

    public LoginViewModel(RemoteUserDataSource remoteUserDataSource, LocalUserDataSource localUserDataSource) {
        mLoginUseCase = new LoginUseCase(remoteUserDataSource, localUserDataSource);
    }

    public void onLogin(String userName, String userPwd) {
        loading.postValue(true);

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            failMsg.postValue("empty.");
            loading.postValue(false);
            return;
        }

        LoginUseCase.LoginParams p = new LoginUseCase.LoginParams();
        p.userName = userName;
        p.userPwd = userPwd;

        mLoginUseCase.invokeAsync(p, new ControllerCallback<User>() {
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
