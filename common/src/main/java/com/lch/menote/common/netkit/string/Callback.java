package com.lch.menote.common.netkit.string;

/**
 * Created by lichenghang on 2017/12/3.
 */

public interface Callback<T> {

    void onSuccess(T result);

    void onFail(String msg);
}
