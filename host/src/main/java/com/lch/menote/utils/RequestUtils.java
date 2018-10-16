package com.lch.menote.utils;

import com.blankj.utilcode.util.EncryptUtils;
import com.lch.menote.ApiConstants;
import com.lch.netkit.v2.apirequest.ApiRequestParams;
import com.lch.netkit.v2.filerequest.UploadFileParams;

/**
 * Created by lichenghang on 2018/6/9.
 */

public final class RequestUtils {

    public static String buildHeaderSign(String ts) {
        return EncryptUtils.encryptMD5ToString(ApiConstants.APP_KEY + ts);
    }

    public static ApiRequestParams minoteStringRequestParams() {
        ApiRequestParams params = new ApiRequestParams();

        String ts = System.currentTimeMillis() + "";
        params.addHeader("ts", ts);
        params.addHeader("sign", RequestUtils.buildHeaderSign(ts));

        return params;
    }

    public static UploadFileParams minoteUploadFileParams() {
        UploadFileParams params = UploadFileParams.newInstance();

        String ts = System.currentTimeMillis() + "";
        params.addHeader("ts", ts);
        params.addHeader("sign", RequestUtils.buildHeaderSign(ts));

        return params;
    }
}
