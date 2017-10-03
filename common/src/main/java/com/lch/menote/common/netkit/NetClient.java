package com.lch.menote.common.netkit;

import okhttp3.OkHttpClient;

/**
 * Created by lichenghang on 2017/10/3.
 */

public final class NetClient {

    private static final OkHttpClient client = new OkHttpClient();

    public static OkHttpClient ok() {
        return client;
    }
}
