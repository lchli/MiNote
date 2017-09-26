package com.lch.menote.note.ui

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.lch.menote.common.WRAP_CONTENT
import com.lch.menote.note.R
import kotlinx.android.synthetic.main.note_tune_view.view.*

/**
 * Created by lichenghang on 2017/9/24.
 */
internal class TuneView(ctx: Context) : LinearLayout(ctx) {

    init {
        View.inflate(context, R.layout.note_tune_view, this)
    }

    internal fun setResource(datas: String) {
        parentView.removeAllViews()

        val tv = TextView(context)
        tv.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        tv.setEms(1)
        tv.text = datas
        tv.gravity=Gravity.CENTER_HORIZONTAL
        tv.setLineSpacing(0f,0.8f)
        //tv.setBackgroundColor(Color.RED)

        parentView.addView(tv)


    }

}