package com.lch.menote.note.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import com.lch.menote.common.util.ScreenHelper
import com.lch.menote.note.helper.NoteUtils


class URLDrawable(context: Context, private val imageName: String) : BitmapDrawable() {

    private val bound: Rect
    private var drawable: Drawable? = null

    init {
        this.bound = getDefaultImageBounds(context)
        this.bounds = bound
    }

    fun setDrawable(drawable: Drawable) {
        this.drawable = drawable
        drawable.bounds = bound
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        if (drawable != null) {
            drawable!!.draw(canvas)
        }
    }

    private fun getDefaultImageBounds(context: Context): Rect {
        val width = ScreenHelper.getScreenWidth(context)
        val size = NoteUtils.parseNoteImageSize(imageName)
        val w = size[0]
        val h = size[1]/2
        return Rect((width - w) / 2, 0, (width - w) / 2 + w, h)

    }
}