package com.lch.menote.common

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.lch.menote.common.util.UiHandler
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener

/**
 * Created by Administrator on 2017/9/21.
 */
private val GLOBAL_TAG = "MiNote"
private val LOG_ENABLE = BuildConfig.DEBUG

fun Context.launchActivity(clazz: Class<out Activity>) {
    val it = Intent(this, clazz)
    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(it)
}

fun Context.toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
    if (msg == null) {
        return
    }
    UiHandler.post({
        Toast.makeText(this, msg, duration).show()
    })
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


fun Context.logIfDebug(msg: String) {
    if (LOG_ENABLE) {
        log(msg)
    }
}

fun Context.showListDialog(listener: OnItemClickListener, expand: Boolean = false, items: List<String>): DialogPlus {
    val adp = ArrayAdapter<String>(this, R.layout.simple_expandable_list_item_1, items)
    val dia = DialogPlus.newDialog(this)
            .setAdapter(adp)
            .setOnItemClickListener(listener)
            .setExpanded(expand)
            .create()
    dia.show()
    return dia
}
