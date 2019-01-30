package com.lch.menote.user.presenterx;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lch.menote.note.NoteApiManager;
import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.domain.LoginUseCase;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ControllerCallback;

/**
 * presenter负责将model转换成视图需要的viewmodel
 */
public class LoginPresenter {

    public interface MvpView {
        void showLoading();

        void dismissLoading();

        void showFail(String msg);

        void toUserCenter();

    }

    private final MvpView view;
    private final LoginUseCase mLoginUseCase = new LoginUseCase(UserModuleInjector.getINS().provideRemoteUserDataSource(), UserModuleInjector.getINS().provideLocalUserDataSource());

    public LoginPresenter(@NonNull MvpView view) {
        this.view = view;
    }

    public void login(String userName, String userPwd) {

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            view.showFail("empty.");
            return;
        }

        LoginUseCase.LoginParams p = new LoginUseCase.LoginParams();
        p.userName = userName;
        p.userPwd = userPwd;

        view.showLoading();
        mLoginUseCase.invokeAsync(p, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                view.dismissLoading();

                if (user == null) {
                    view.showFail("user is null.");
                    return;
                }

                NoteApiManager.getINS().onUserLogin();

                view.toUserCenter();
            }

            @Override
            public void onError(int i, String s) {
                view.showFail(s);
                view.dismissLoading();
            }
        });
    }


}
