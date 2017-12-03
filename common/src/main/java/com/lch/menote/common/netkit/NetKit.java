package com.lch.menote.common.netkit;

import android.os.Handler;
import android.os.Looper;

import com.lch.menote.common.netkit.file.FileManager;
import com.lch.menote.common.netkit.string.StringRequest;

import okhttp3.OkHttpClient;

/**
 * Created by lichenghang on 2017/10/3.
 */

public final class NetKit {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    private static OkHttpClient client = new OkHttpClient();
    private static final FileManager fileManager = new FileManager();
    private static final StringRequest stringRequest = new StringRequest();

    public static OkHttpClient client() {
        return client;
    }

    public static void setClient(OkHttpClient client) {
        NetKit.client = client;
    }

    public static FileManager fileRequest() {
        return fileManager;
    }

    public static StringRequest stringRequest() {
        return stringRequest;

    }

    public static void runAsync(Runnable r) {
        client().dispatcher().executorService().execute(r);
    }

    public static void runInUI(Runnable r) {
        handler.post(r);
    }

    public static void setLogEnable(boolean b) {

    }
}
