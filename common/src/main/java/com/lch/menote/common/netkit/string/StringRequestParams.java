package com.lch.menote.common.netkit.string;

import com.lch.menote.common.netkit.file.helper.CommonParams;

import okhttp3.RequestBody;

/**
 * Created by lichenghang on 2017/12/3.
 */

public class StringRequestParams extends CommonParams<StringRequestParams> {

    private RequestBody requestBody;


    public StringRequestParams setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return thisObject();
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    @Override
    protected StringRequestParams thisObject() {
        return this;
    }


}
