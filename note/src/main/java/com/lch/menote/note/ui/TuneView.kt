package com.lch.menote.note.ui

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.lch.menote.common.WRAP_CONTENT

/**
 * Created by lichenghang on 2017/9/24.
 */
internal class TuneView(ctx: Context) : TextView(ctx) {

    init {
        layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setEms(1)
        gravity = Gravity.CENTER_HORIZONTAL
        setLineSpacing(0f, 0.8f)
        textSize = 16f
    }


}