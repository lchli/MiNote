package com.lch.menote.user.dataimpl;

import com.lch.menote.ApiConstants;
import com.lch.menote.app.model.ApkResponse;
import com.lch.menote.user.datainterface.AppUpdateInfoDataSource;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.v2.NetKit;
import com.lch.netkit.v2.apirequest.ApiRequestParams;
import com.lch.netkit.v2.common.NetworkResponse;
import com.lch.netkit.v2.parser.Parser;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.utils.tool.AliJsonHelper;

/**
 * Created by Administrator on 2018/12/27.
 */

public class NetAppUpdateInfoSource implements AppUpdateInfoDataSource {

    @Override
    public ResponseValue<String> getUpdateInfo(int currentVersionCode) {
        ResponseValue<String> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams();
        params.setUrl(ApiConstants.APK_UPDATE);
        params.addParam("currentVersionCode", currentVersionCode + "");

        NetworkResponse<ApkResponse> res = NetKit.apiRequest().syncGet(params, new Parser<ApkResponse>() {
            @Override
            public ApkResponse parse(String s) {
                return AliJsonHelper.parseObject(s, ApkResponse.class);
            }
        });

        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null || res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        ret.data = res.data.data;

        return ret;

    }
}
