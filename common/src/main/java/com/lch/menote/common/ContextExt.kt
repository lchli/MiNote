package com.lch.menote.common

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Created by Administrator on 2017/9/21.
 */
fun Context.launchActivity(clazz: Class<out Activity>) {
    val it = Intent(this, clazz)
    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(it)
}