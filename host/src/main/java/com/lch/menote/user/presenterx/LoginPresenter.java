package com.lch.menote.user.presenterx;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.domain.LoginUseCase;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ControllerCallback;

/**
 * presenter负责将model转换成视图需要的viewmodel
 */
public class LoginPresenter {

    public interface View {
        void renderLoading();

        void dismissLoading();

        void renderLoginSuccess();

        void renderLoginFail(String msg);

    }

    private final View view;
    private final LoginUseCase mLoginUseCase;

    public LoginPresenter(RemoteUserDataSource remoteUserDataSource, UserSessionDataSource localUserDataSource, @NonNull View view) {
        this.view = view;
        mLoginUseCase = new LoginUseCase(remoteUserDataSource, localUserDataSource);
    }

    public void login(String userName, String userPwd) {
        view.renderLoading();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            view.renderLoginFail("empty.");
            view.dismissLoading();
            return;
        }

        LoginUseCase.LoginParams p = new LoginUseCase.LoginParams();
        p.userName = userName;
        p.userPwd = userPwd;

        mLoginUseCase.invokeAsync(p, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                view.dismissLoading();

                if (user == null) {
                    view.renderLoginFail("user is null.");
                    return;
                }

                view.renderLoginSuccess();
            }

            @Override
            public void onError(int i, String s) {
                view.renderLoginFail(s);
                view.dismissLoading();
            }
        });
    }


}
