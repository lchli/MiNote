package com.lch.menote.app;

import com.lch.menote.ApiConstants;
import com.lch.menote.app.model.ApkResponse;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.v2.NetKit;
import com.lch.netkit.v2.apirequest.ApiRequestParams;
import com.lch.netkit.v2.common.NetworkResponse;
import com.lch.netkit.v2.parser.Parser;
import com.lchli.utils.tool.AliJsonHelper;


/**
 * Created by lichenghang on 2018/6/9.
 */

public class ApkRepo {

    public NetworkResponse<ApkResponse> getUpdate(int currentVersionCode) {

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.APK_UPDATE);
        params.addParam("currentVersionCode", currentVersionCode + "");


        return NetKit.apiRequest().syncGet(params, new Parser<ApkResponse>() {
            @Override
            public ApkResponse parse(String s) {
                return AliJsonHelper.parseObject(s, ApkResponse.class);
            }
        });

    }


}
