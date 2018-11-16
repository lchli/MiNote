package com.lch.menote.user.ui;

import android.support.annotation.Nullable;

import com.lch.menote.DI;
import com.lch.menote.user.data.LocalUserDataSource;
import com.lch.menote.user.data.RemoteUserDataSource;
import com.lch.menote.user.data.UserRepo;
import com.lch.menote.user.domain.LoginUseCase;
import com.lch.menote.user.domain.RegisterUseCase;
import com.lch.menote.user.domain.SaveUserSessionUseCase;
import com.lch.menote.user.domain.UpdateUseCase;
import com.lch.menote.user.domain.UploadFileUseCase;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ControllerCallback;

/**
 * 需要保证：mock数据源在demo里面传递进来。
 */
public class UserPrensenter {

    private final LoginUseCase mLoginUseCase = new LoginUseCase(new UserRepo(DI.provideRemoteUserDataSource()));
    private final UpdateUseCase updateUseCase = new UpdateUseCase(new UserRepo(DI.provideRemoteUserDataSource()));
    private final UploadFileUseCase uploadFileUseCase = new UploadFileUseCase();
    private final RegisterUseCase mRegisterUseCase = new RegisterUseCase(DI.provideRemoteUserDataSource());
    private final SaveUserSessionUseCase mSaveUserSessionUseCase = new SaveUserSessionUseCase(DI.provideLocalUserDataSource());

    public UserPrensenter(LocalUserDataSource localUserDataSource, RemoteUserDataSource remoteUserDataSource) {
    }

    public void register(String userName, String userPwd, String userHeadUrl, final ControllerCallback<User> result) {
        RegisterUseCase.RegisterParams p = new RegisterUseCase.RegisterParams();
        p.userName = userName;
        p.userPwd = userPwd;
        p.userHeadUrl = userHeadUrl;

        mRegisterUseCase.invokeAsync(p, result);
    }

    public void login(String userName, String userPwd, final ControllerCallback<User> result) {
        LoginUseCase.LoginParams p = new LoginUseCase.LoginParams();
        p.userName = userName;
        p.userPwd = userPwd;

        mLoginUseCase.invokeAsync(p, result);
    }

    public void update(final User user, final String userHeadImgPath, final ControllerCallback<User> cb) {
        UploadFileUseCase.Params fileParam = new UploadFileUseCase.Params();
        fileParam.userHeadImgPath = userHeadImgPath;

        uploadFileUseCase.invokeAsync(fileParam, new ControllerCallback<String>() {
            @Override
            public void onSuccess(@Nullable String s) {
                user.headUrl = s;

                UpdateUseCase.UpdateParams p = new UpdateUseCase.UpdateParams();
                p.user = user;

                updateUseCase.invokeAsync(p, cb);
            }

            @Override
            public void onError(int i, String s) {
                cb.onError(i, s);

            }
        });


    }

    public void savaUserSession(User session, ControllerCallback<Void> cb) {
        SaveUserSessionUseCase.Params p = new SaveUserSessionUseCase.Params();
        p.session = session;

        mSaveUserSessionUseCase.invokeAsync(p, cb);
    }
}
