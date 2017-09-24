package com.lch.menote.note.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup

/**
 * Created by lichenghang on 2017/9/24.
 */
class TuneView(ctx:Context):View(ctx) {

    val p= Paint()


    init {
        layoutParams= ViewGroup.LayoutParams(100,100)
        p.color= Color.RED
        p.textSize=42f
        setBackgroundColor(Color.BLUE)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(canvas==null)return

        val s=width/2f
        var e=height/2f

        canvas.drawText(".",s,e,p)
        val h=20
        e+=h
        canvas.drawText("8",s,e,p)
        e+=h
        canvas.drawText(".",s,e,p)
        e+=h
        canvas.drawText("-",s,e,p)
    }
}