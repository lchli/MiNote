package com.lch.menote.note.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.lch.menote.kotlinext.evalFontHeight
import com.lch.menote.note.domain.MusicData


/**
 * Created by Administrator on 2017/9/25.
 */
class MusicGridView : RecyclerView {

    private val p = Paint()
    private var mDatas: List<MusicData>? = null

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    init {
        p.color = Color.RED
        p.textSize = 42f

    }

    fun drawMusic(datas: List<MusicData>) {
        mDatas = datas
        invalidate()
    }

    private fun drawLines(v1: View?, v2: View?, canvas: Canvas?, text: String?) {
        if(v1==null||v2==null){
            return
        }

        val oval = RectF(v1.left.toFloat() + v1.width / 2, v1.top.toFloat(), v2.left.toFloat() + v2.width / 2, v2.top.toFloat() + v2.height / 2)

        p.style = Paint.Style.STROKE
        canvas!!.drawArc(oval, 0f, -180f, false, p)
        if (text != null) {
            p.style = Paint.Style.FILL
            canvas.drawText(text, oval.centerX(), oval.top + context.evalFontHeight(p) / 2, p)
        }
    }

    private fun dispatchDrawMusicImpl(from: MusicData, to: MusicData, canvas: Canvas, linktext: String?) {
        drawLines(getChildAt(mDatas!!.indexOf(from)), getChildAt(mDatas!!.indexOf(to)), canvas, linktext)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        dispatchDrawMusic(canvas)

        super.dispatchDraw(canvas)
    }

    private fun dispatchDrawMusic(canvas: Canvas?) {
        if (mDatas == null) return

        for (i in 0 until mDatas!!.size) {
            val links = mDatas!![i].links

            for ((data, text) in links) {
                dispatchDrawMusicImpl(mDatas!![i], data, canvas!!, text)
            }
        }


    }
}