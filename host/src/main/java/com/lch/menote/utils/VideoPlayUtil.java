package com.lch.menote.utils;

import android.text.TextUtils;

public final class VideoPlayUtil {

    public static boolean isShouldCache(String videoUrl) {
        if (TextUtils.isEmpty(videoUrl)) {
            return false;
        }

        return videoUrl.startsWith("http://") || videoUrl.startsWith("https://");
    }
}
