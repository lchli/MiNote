package com.lch.menote.common.netkit.file.helper;

/**
 * Created by Administrator on 2017/9/30.
 */

public class NetworkError {

    public int code;
    public String msg;

    public NetworkError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public NetworkError(String msg) {
        this.msg = msg;
    }
}
