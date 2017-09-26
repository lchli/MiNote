package com.lch.menote.common

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup

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