package com.lch.menote.common.util;

import java.util.UUID;

/**
 * Created by lchli on 2016/8/13.
 */

public class UUIDUtils {

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
