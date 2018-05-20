package com.lch.menote.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lichenghang on 2018/5/19.
 */

public final class Navigator {

    public static void launchActivity(Context context, Class<? extends Activity> clazz) {

        Intent it = new Intent(context, clazz);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);

    }
}
