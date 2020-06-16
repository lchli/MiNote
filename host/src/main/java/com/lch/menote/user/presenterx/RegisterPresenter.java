package com.lch.menote.user.presenterx;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lch.menote.file.FileModuleInjector;
import com.lch.menote.note.NoteApiManager;
import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.domain.RegisterUseCase;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ControllerCallback;

/**
 * presenter负责将model转换成视图需要的viewmodel
 */
public class RegisterPresenter {

    public interface MvpView {
        void showLoading();

        void dismissLoading();

        void showFail(String msg);

        void toUserCenter();

    }

    private final MvpView view;
    private final RegisterUseCase mRegisterUseCase = new RegisterUseCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource(), FileModuleInjector.getINS().provideRemoteFileSource());

    public RegisterPresenter(@NonNull MvpView view) {
        this.view = view;
    }

    public void register(String userName, String userPwd, String headPath) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            view.showFail("empty.");
            return;
        }

        RegisterUseCase.RegisterParams p = new RegisterUseCase.RegisterParams();
        p.userName = userName;
        p.userPwd = userPwd;
        p.userHeadPath = headPath;

        view.showLoading();
        mRegisterUseCase.invokeAsync(p, new ControllerCallback<User>() {
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
