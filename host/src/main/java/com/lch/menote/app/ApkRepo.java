package com.lch.menote.app;

import com.lch.menote.ApiConstants;
import com.lch.menote.app.model.ApkResponse;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.string.Parser;
import com.lch.netkit.string.StringRequestParams;

/**
 * Created by lichenghang on 2018/6/9.
 */

public class ApkRepo {

    public ResponseValue<ApkResponse> getUpdate(int currentVersionCode) {

        StringRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.APK_UPDATE);
        params.addParam("currentVersionCode", currentVersionCode+"");


        return NetKit.stringRequest().getSync(params, new Parser<ApkResponse>() {
            @Override
            public ApkResponse parse(String s) {
                return AliJsonHelper.parseObject(s, ApkResponse.class);
            }
        });

    }


}
