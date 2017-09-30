package com.lch.menote.common.netkit.file.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 通用参数，比如设置header，url，普通参数等。
 * Created by bbt-team on 2017/8/1.
 */

public abstract class CommonParams<T extends CommonParams> {

    private final Map<String, String> params = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private String url;


    protected abstract T thisObject();

    public T addParam(String key, String value) {
        params.put(key, value);
        return thisObject();
    }

    public T addHeader(String key, String value) {
        headers.put(key, value);
        return thisObject();
    }

    public Iterator<Map.Entry<String, String>> textParams() {
        return params.entrySet().iterator();
    }

    public String textParam(String key) {
        return params.get(key);
    }

    public Iterator<Map.Entry<String, String>> headers() {
        return headers.entrySet().iterator();
    }

    public String getUrl() {
        return url;
    }

    public T setUrl(String url) {
        this.url = url;
        return thisObject();
    }
}
