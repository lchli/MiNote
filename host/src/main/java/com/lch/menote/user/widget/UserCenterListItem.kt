package com.lch.menote.user.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.lch.menote.R
import kotlinx.android.synthetic.main.user_list_item.view.*

class UserCenterListItem : FrameLayout {


    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.user_list_item, this)


        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.UserCenterListItem)
            val text = a.getString(R.styleable.UserCenterListItem_UserCenterListItem_text)
            val icon = a.getDrawable(R.styleable.UserCenterListItem_UserCenterListItem_icon)
            a.recycle()
            if (text != null) {
                text_widget.setText(text)
            }
            if (icon != null) {
                icon_widget.setImageDrawable(icon)
            }
        }
    }

    fun setText(text: String?) {
        text_widget.setText(text)
    }

    fun setIcon(resId: Int) {
        icon_widget.setImageResource(resId)
    }
}