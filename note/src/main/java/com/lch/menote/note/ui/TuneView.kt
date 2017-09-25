package com.lch.menote.note.ui

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.evalFontHeight
import com.lch.menote.common.log
import com.lch.menote.note.R

/**
 * Created by lichenghang on 2017/9/24.
 */
class TuneView(ctx: Context) : View(ctx) {

    val p = Paint()
    val rec = RectF(0f, 0f, 100f, 100f)
val bmp=BitmapFactory.decodeResource(resources,R.drawable.ic_add_note)

    init {
        layoutParams = ViewGroup.LayoutParams(1000, 1000)
        p.color = Color.RED
        p.textSize = 42f
        p.style = Paint.Style.STROKE
       // setBackgroundColor(Color.BLUE)


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        val fontH = context.evalFontHeight(p)
        context.log("fontH=$fontH")
        context.log("height=$height")

        val dig = "7"

        val x = left+(width - p.measureText(dig)) / 2f
        var y = height.toFloat()//top+(height -fontH) / 2f
        context.log("y=$y")

        canvas.drawBitmap(bmp,0f,0f,p)
        canvas.drawBitmap(bmp,bmp.width.toFloat(),0f,p)
        canvas.drawArc(0f,0f,50f,50f,90f,20f,false,p)

//
//        canvas.drawText(dig, x, y, p)
//
//        y += fontH
//
//        canvas.drawText(".", x, y, p)
//
//        y += fontH
//
//        canvas.drawText("-", x, y, p)
    }
}