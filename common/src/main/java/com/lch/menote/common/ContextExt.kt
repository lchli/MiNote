package com.lch.menote.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.widget.Toast

/**
 * Created by Administrator on 2017/9/21.
 */
private val GLOBAL_TAG = "MiNote"

fun Context.launchActivity(clazz: Class<out Activity>) {
    val it = Intent(this, clazz)
    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(it)
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Context.launchActivity(it: Intent) {
    if (this !is Activity) {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(it)
}

fun Context.evalFontHeight(p: Paint): Float {
    return Math.ceil((p.fontMetrics.descent - p.fontMetrics.ascent).toDouble()).toFloat()
}

fun Context.log(tag: String, msg: String) {
    Log.e(tag, msg)
}


fun Context.log(msg: String) {
    log(GLOBAL_TAG, msg)
}