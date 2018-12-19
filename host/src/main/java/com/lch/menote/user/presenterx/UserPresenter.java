package com.lch.menote.user.presenterx;

import android.support.annotation.Nullable;

import com.lch.menote.file.data.RemoteFileSource;
import com.lch.menote.user.datainterface.LocalUserDataSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.domain.LoginUseCase;
import com.lch.menote.user.domain.RegisterUseCase;
import com.lch.menote.user.domain.SaveUserSessionUseCase;
import com.lch.menote.user.domain.UpdateUseCase;
import com.lch.menote.user.domain.UploadFileUseCase;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ControllerCallback;

import java.io.File;

/**
 * Presenter负责创建用例，它只依赖于数据源接口。
 * 需要保证：mock数据源在demo里面传递进来。
 */
public final class UserPresenter {

    private final LoginUseCase mLoginUseCase;
    private final UpdateUseCase updateUseCase;
    private final UploadFileUseCase uploadFileUseCase;
    private final RegisterUseCase mRegisterUseCase;
    private final SaveUserSessionUseCase mSaveUserSessionUseCase;

    public UserPresenter(LocalUserDataSource localUserDataSource, RemoteUserDataSource remoteUserDataSource, RemoteFileSource remoteFileSource) {
        mLoginUseCase = new LoginUseCase(remoteUserDataSource);
        updateUseCase = new UpdateUseCase(remoteUserDataSource);
        uploadFileUseCase = new UploadFileUseCase(remoteFileSource);
        mRegisterUseCase = new RegisterUseCase(remoteUserDataSource);

        mSaveUserSessionUseCase = new SaveUserSessionUseCase(localUserDataSource);
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
        fileParam.userHeadImg = new File(userHeadImgPath);

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
                if (cb != null) {
                    cb.onError(i, s);
                }

            }
        });


    }

    public void savaUserSession(User session, ControllerCallback<Void> cb) {
        SaveUserSessionUseCase.Params p = new SaveUserSessionUseCase.Params();
        p.session = session;

        mSaveUserSessionUseCase.invokeAsync(p, cb);
    }
}
