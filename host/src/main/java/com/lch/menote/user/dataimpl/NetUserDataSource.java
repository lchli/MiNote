package com.lch.menote.user.dataimpl;

import com.apkfuns.logutils.LogUtils;
import com.lch.menote.ApiConstants;
import com.lch.menote.user.dataimpl.response.LoginResponse;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.route.User;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.v2.NetKit;
import com.lch.netkit.v2.apirequest.ApiRequestParams;
import com.lch.netkit.v2.common.NetworkResponse;
import com.lch.netkit.v2.parser.Parser;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.utils.tool.AliJsonHelper;

/**
 * 数据访问接口，应该使用业务模型model或普通参数作为传递。
 */
public class NetUserDataSource implements RemoteUserDataSource {

    @Override
    public ResponseValue<User> addUser(String userName, String userPwd, String userHeadUrl, String userContact, String nick) {
        ResponseValue<User> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.REGISTER);
        params.addParam("userName", userName);
        params.addParam("userPwd", userPwd);
        params.addParam("userHeadUrl", userHeadUrl);
        params.addParam("userContact", userContact);
        params.addParam("userNick", nick);

        NetworkResponse<LoginResponse> res = NetKit.apiRequest().syncPost(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                LogUtils.e(s);
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null) {
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrorMsg(res.data.getMessage());
            return ret;
        }

        if (res.data.data == null) {
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        ret.data = res.data.data;

        return ret;

    }

    @Override
    public ResponseValue<User> getUser(String userName, String userPwd) {
        ResponseValue<User> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.LOGIN);
        params.addParam("userName", userName);
        params.addParam("userPwd", userPwd);

        NetworkResponse<LoginResponse> res = NetKit.apiRequest().syncGet(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                LogUtils.e(s);
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }


        if (res.data == null) {
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrorMsg(res.data.getMessage());
            return ret;
        }

        if (res.data.data == null) {
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        ret.data = res.data.data;

        return ret;
    }


    @Override
    public ResponseValue<User> updateUser(UpdateUserParams updateUserParams) {
        ResponseValue<User> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.USER_UPDATE);
        params.addParam("userPwd", updateUserParams.pwd);
        params.addParam("userHeadUrl", updateUserParams.headUrl);
        params.addParam("userContact", updateUserParams.userContact);
        params.addParam("userNick", updateUserParams.userNick);

        NetworkResponse<LoginResponse> res = NetKit.apiRequest().syncPost(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null || res.data.data == null) {
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        ret.data = res.data.data;

        return ret;
    }

    @Override
    public ResponseValue<User> getUser(String userId) {
        ResponseValue<User> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.USER_GET_BY_ID);
        params.addParam("userId", userId);

        NetworkResponse<LoginResponse> res = NetKit.apiRequest().syncGet(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null || res.data.data == null) {
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        ret.data = res.data.data;

        return ret;
    }
}
