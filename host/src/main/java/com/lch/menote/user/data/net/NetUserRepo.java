package com.lch.menote.user.data.net;

import com.lch.menote.ApiConstants;
import com.lch.menote.user.domain.LoginResponse;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.string.Parser;
import com.lch.netkit.string.StringRequestParams;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class NetUserRepo {

    public ResponseValue<LoginResponse> save(String userName, String userPwd){

        StringRequestParams params= new StringRequestParams();
        params.setUrl(ApiConstants.REGISTER);
        params.addParam("userName",userName);
        params.addParam("userPwd",userPwd);

        return NetKit.stringRequest().postSync(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s,LoginResponse.class);
            }
        });

    }


    public ResponseValue<LoginResponse> get(String userName, String userPwd){

        StringRequestParams params= new StringRequestParams();
        params.setUrl(ApiConstants.LOGIN);
        params.addParam("userName",userName);
        params.addParam("userPwd",userPwd);

        return NetKit.stringRequest().getSync(params, new Parser<LoginResponse>() {
            @Override
            public LoginResponse parse(String s) {
                return AliJsonHelper.parseObject(s,LoginResponse.class);
            }
        });
    }
}
