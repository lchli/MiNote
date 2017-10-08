package com.lch.menote.common

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.util.BitmapScaleUtil
import com.lch.menote.common.util.ToastUtils
import com.lch.menote.common.util.UUIDUtils
import com.lch.route.noaop.Android
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.File


/**
 * Created by Administrator on 2017/9/25.
 */
const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT

fun View.drawBitmap(): Bitmap {
    this.isDrawingCacheEnabled = true
    this.buildDrawingCache()
    return this.drawingCache
}


fun View.saveViewBmpToSdcard() {
    val viewBmp = this.drawBitmap()
    val destFile = File(Environment.getExternalStorageDirectory(), UUIDUtils.uuid() + ".jpg")

    val job = async(CommonPool) {
        val ret = BitmapScaleUtil.saveBitmap(viewBmp, destFile, 100)
        viewBmp.recycle()

        ret
    }

    launch(Android) {

        val isSuccess = job.await()
        if (!isSuccess) {
            ToastUtils.systemToast("图片保存失败")
        } else {
            ToastUtils.systemToast("图片已保存")

            val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            scanIntent.data = Uri.fromFile(destFile)
            getContext().sendBroadcast(scanIntent)


        }
    }
}


fun View.saveViewBmpToSdcard2() {

    val view = this


    val viewBmp = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.RGB_565)
    val canvas = Canvas(viewBmp)
    view.draw(canvas)

    val destFile = File(Environment.getExternalStorageDirectory(), UUIDUtils.uuid() + ".jpg")

    val job = async(CommonPool) {
        val ret = BitmapScaleUtil.saveBitmap(viewBmp, destFile, 100)
        viewBmp.recycle()

        ret
    }

    launch(Android) {

        val isSuccess = job.await()
        if (!isSuccess) {
            ToastUtils.systemToast("图片保存失败")
        } else {
            ToastUtils.systemToast("图片已保存")

            val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            scanIntent.data = Uri.fromFile(destFile)
            getContext().sendBroadcast(scanIntent)


        }
    }
}








