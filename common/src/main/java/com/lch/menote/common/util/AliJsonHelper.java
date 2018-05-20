package com.lch.menote.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by bbt-team on 2017/12/1.
 */

public final class AliJsonHelper {

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            return JSON.parseObject(text, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJSONString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject newJSONObject() {
        return new JSONObject();
    }

    public static  <T> List<T> parseArray(String text, Class<T> clazz) {
        try {
            return JSON.parseArray(text, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }


}
