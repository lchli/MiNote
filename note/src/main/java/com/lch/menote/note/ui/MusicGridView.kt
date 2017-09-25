package com.lch.menote.note.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.GridView
import com.lch.menote.common.evalFontHeight
import com.lch.menote.note.domain.MusicData


/**
 * Created by Administrator on 2017/9/25.
 */
class MusicGridView : GridView {

    private val p = Paint()
    private var mDatas: List<MusicData>? = null

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    init {
        p.color = Color.RED
        p.textSize = 42f
        p.style = Paint.Style.STROKE
    }

    fun drawMusic(datas: List<MusicData>) {
        mDatas = datas
        invalidate()
    }

    private fun drawLines(v1: View, v2: View, canvas: Canvas?, text: String?) {
        val oval = RectF(v1.left.toFloat() + v1.width / 2, v1.top.toFloat(), v2.left.toFloat() + v2.width / 2, v2.top.toFloat() + v2.height / 2)
        // context.log(oval.toString())

        canvas!!.drawArc(oval, 0f, -180f, false, p)
        if (text != null) {
            canvas.drawText(text, oval.centerX(), oval.top + context.evalFontHeight(p) / 2, p)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {

        super.dispatchDraw(canvas)
        dispatchDrawMusic(canvas)
    }

    private fun dispatchDrawMusic(canvas: Canvas?) {
        if (mDatas == null) return

        for (i in 0 until childCount) {
            val links = mDatas!![i].links ?: continue

            for ((id, text) in links) {
                drawLines(getChildAt(i), getChildAt(id), canvas, text)
            }
        }


    }
}