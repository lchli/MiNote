package com.lch.menote.user.data.net;

import com.lch.menote.ApiConstants;
import com.lch.menote.user.data.sp.SpUserRepo;
import com.lch.menote.user.domain.LoginResponse;
import com.lch.menote.user.route.User;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.string.Parser;
import com.lch.netkit.string.StringRequestParams;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class NetUserRepo {

    public ResponseValue<LoginResponse> save(String userName, String userPwd, String userHeadUrl) {

        StringRequestParams params = new StringRequestParams();
        params.setUrl(ApiConstants.REGISTER);
        params.addParam("userName", userName);
        params.addParam("userPwd", userPwd);
        params.addParam("userHeadUrl", userHeadUrl);

        return NetKit.stringRequest().postSync(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

    }


    public ResponseValue<LoginResponse> update(User u) {

        StringRequestParams params = new StringRequestParams();
        params.setUrl(ApiConstants.USER_UPDATE);
        params.addParam("userName", u.name);
        params.addParam("userPwd", u.pwd);
        params.addParam("userId", u.uid);
        params.addParam("userHeadUrl", u.headUrl);
        params.addParam("token", u.token);
        params.addParam("userContact", u.userContact);

        return NetKit.stringRequest().postSync(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });

    }

    public ResponseValue<LoginResponse> get(String userName, String userPwd) {

        StringRequestParams params = new StringRequestParams();
        params.setUrl(ApiConstants.LOGIN);
        params.addParam("userName", userName);
        params.addParam("userPwd", userPwd);

        return NetKit.stringRequest().getSync(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });
    }

    public ResponseValue<LoginResponse> getById(String userId) {
        ResponseValue<LoginResponse> ret = new ResponseValue<>();

        ResponseValue<User> se = SpUserRepo.getINS().getUser();
        if (se.hasError()) {
            ret.setErrMsg(se.errMsg());
            return ret;
        }

        if (se.data == null) {
            ret.setErrMsg("not login");
            return ret;
        }

        StringRequestParams params = new StringRequestParams();
        params.setUrl(ApiConstants.USER_GET_BY_ID);
        params.addParam("userId", userId);
        params.addParam("token", se.data.token);

        return NetKit.stringRequest().getSync(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s, LoginResponse.class);
            }
        });
    }
}
