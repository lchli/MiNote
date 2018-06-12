package com.lch.menote.utils;

import com.blankj.utilcode.util.EncryptUtils;
import com.lch.menote.ApiConstants;
import com.lch.netkit.file.helper.UploadFileParams;
import com.lch.netkit.string.StringRequestParams;

/**
 * Created by lichenghang on 2018/6/9.
 */

public final class RequestUtils {

    public static String buildHeaderSign(String ts) {
        return EncryptUtils.encryptMD5ToString( ApiConstants.APP_KEY+ts);
    }

    public static StringRequestParams minoteStringRequestParams() {
        StringRequestParams params = new StringRequestParams();

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
