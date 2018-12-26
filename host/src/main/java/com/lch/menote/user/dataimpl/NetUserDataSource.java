package com.lch.menote.user.dataimpl;

import com.lch.menote.ApiConstants;
import com.lch.menote.user.dataimpl.response.LoginResponse;
import com.lch.menote.user.datainterface.RemoteUserDataSource;
import com.lch.menote.user.route.User;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.v2.NetKit;
import com.lch.netkit.v2.apirequest.ApiRequestParams;
import com.lch.netkit.v2.common.NetworkResponse;
import com.lch.netkit.v2.parser.Parser;

/**
 * 数据访问接口，应该使用业务模型model或普通参数作为传递。
 */
public class NetUserDataSource implements RemoteUserDataSource {

    @Override
    public ResponseValue<User> addUser(String userName, String userPwd, String userHeadUrl) {
        ResponseValue<User> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.REGISTER);
        params.addParam("userName", userName);
        params.addParam("userPwd", userPwd);
        params.addParam("userHeadUrl", userHeadUrl);

        NetworkResponse<LoginResponse> res = NetKit.apiRequest().syncPost(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

        if (res.hasError()) {
            ret.code = res.httpCode;
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null || res.data.data == null) {
            ret.code = res.httpCode;
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
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

        if (res.hasError()) {
            ret.code = res.httpCode;
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null || res.data.data == null) {
            ret.code = res.httpCode;
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        ret.data = res.data.data;

        return ret;
    }


    @Override
    public ResponseValue<User> updateUser(String updateUserId, String sessionUid, String sessionToken, UpdateUserParams updateUserParams) {
        ResponseValue<User> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.USER_UPDATE);
        params.addParam("userName", updateUserParams.name);
        params.addParam("userPwd", updateUserParams.pwd);
        params.addParam("userId", updateUserId);
        params.addParam("userHeadUrl", updateUserParams.headUrl);
        params.addParam("token", sessionToken);
        params.addParam("userContact", updateUserParams.userContact);
        params.addParam("myUserId", sessionUid);

        NetworkResponse<LoginResponse> res = NetKit.apiRequest().syncPost(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

        if (res.hasError()) {
            ret.code = res.httpCode;
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null || res.data.data == null) {
            ret.code = res.httpCode;
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        ret.data = res.data.data;

        return ret;
    }

    @Override
    public ResponseValue<User> getUser(String userId, String sessionUid, String sessionToken) {
        ResponseValue<User> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.USER_GET_BY_ID);
        params.addParam("userId", userId);
        params.addParam("token", sessionToken);
        params.addParam("myUserId", sessionUid);

        NetworkResponse<LoginResponse> res = NetKit.apiRequest().syncGet(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

        if (res.hasError()) {
            ret.code = res.httpCode;
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null || res.data.data == null) {
            ret.code = res.httpCode;
            ret.setErrorMsg("return data is null.");
            return ret;
        }

        ret.data = res.data.data;

        return ret;
    }
}
