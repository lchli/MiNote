package com.lch.menote.user.domain;

import android.text.TextUtils;

import com.lch.menote.file.datainterface.RemoteFileSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

import java.io.File;

public class RegisterUseCase extends UseCase<RegisterUseCase.RegisterParams, User> {

    public static class RegisterParams {

        public String userName;
        public String userPwd;
        public String userHeadPath;
    }


    private RemoteUserDataSource userDataSource;
    private final UserSessionDataSource localUserDataSource;
    private RemoteFileSource remoteFileSource;

    public RegisterUseCase(RemoteUserDataSource userDataSource, UserSessionDataSource localUserDataSource, RemoteFileSource remoteFileSource) {
        this.userDataSource = userDataSource;
        this.localUserDataSource = localUserDataSource;
        this.remoteFileSource = remoteFileSource;
    }

    @Override
    protected ResponseValue<User> execute(RegisterParams parameters) {

        String headUrl = "";

        if (!TextUtils.isEmpty(parameters.userHeadPath)) {
            ResponseValue<String> fileres = remoteFileSource.addFile(new File(parameters.userHeadPath));
            headUrl = fileres.data;

            if (fileres.hasError() || TextUtils.isEmpty(headUrl)) {
                ResponseValue<User> ret = new ResponseValue<>();
                ret.setErrorMsg("head icon upload fail.");
                return ret;
            }
        }

        ResponseValue<User> res = userDataSource.addUser(parameters.userName, parameters.userPwd, headUrl);
        if (res.hasError() || res.data == null) {
            return res;
        }
        localUserDataSource.saveUser(res.data);

        return res;

    }
}
