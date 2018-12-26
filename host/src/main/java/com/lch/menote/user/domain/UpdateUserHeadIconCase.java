package com.lch.menote.user.domain;

import android.text.TextUtils;

import com.lch.menote.file.datainterface.RemoteFileSource;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.datainterface.UserSessionDataSource;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.mvc.UseCase;

import java.io.File;

public class UpdateUserHeadIconCase extends UseCase<UpdateUserHeadIconCase.UpdateParams, User> {

    private final UserSessionDataSource sessionDataSource;

    public static class UpdateParams {
        public String headPath;
    }


    private RemoteUserDataSource dataSource;
    private RemoteFileSource remoteFileSource;

    public UpdateUserHeadIconCase(RemoteUserDataSource dataSource, UserSessionDataSource sessionDataSource, RemoteFileSource remoteFileSource) {
        this.dataSource = dataSource;
        this.sessionDataSource = sessionDataSource;
        this.remoteFileSource = remoteFileSource;
    }

    @Override
    protected ResponseValue<User> execute(UpdateParams parameters) {
        ResponseValue<User> res = new ResponseValue<>();

        if (TextUtils.isEmpty(parameters.headPath)) {
            res.setErrorMsg("head path is null.");
            return res;
        }

        res = sessionDataSource.getUser();
        if (res.hasError()) {
            return res;
        }
        if (res.data == null) {
            res.setErrorMsg("session is null.");
            return res;
        }

        ResponseValue<String> fileres = remoteFileSource.addFile(new File(parameters.headPath));
        if (fileres.hasError() || TextUtils.isEmpty(fileres.data)) {
            res.setErrorMsg("head icon upload fail.");
            return res;
        }


        RemoteUserDataSource.UpdateUserParams updateUserParams = new RemoteUserDataSource.UpdateUserParams();
        updateUserParams.userContact = res.data.userContact;
        updateUserParams.headUrl = fileres.data;
        updateUserParams.name = res.data.name;
        updateUserParams.pwd = res.data.pwd;

        return dataSource.updateUser(res.data.uid, res.data.uid, res.data.token, updateUserParams);
    }
}
