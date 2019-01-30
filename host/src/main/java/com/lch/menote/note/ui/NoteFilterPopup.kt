package com.lch.menote.note.ui

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.lch.menote.R
import com.lch.menote.note.model.NoteFilterData

/**
 * Created by lichenghang on 2016/4/27.
 */
class NoteFilterPopup(private val mContext: Context) : PopupWindow(mContext) {


    private val postFilterEntity = NoteFilterData("", "")
    private var mCallback: Callback? = null


    private var btFilter: TextView

    private var tagEdit: TextView

    init {
        this.isTouchable = true
        this.setTouchInterceptor { v, event -> false }
        this.setBackgroundDrawable(BitmapDrawable())
        this.width = LinearLayout.LayoutParams.MATCH_PARENT
        this.height = LinearLayout.LayoutParams.WRAP_CONTENT
        this.isFocusable = true

        val contentView = LayoutInflater.from(mContext).inflate(
                R.layout.popup_note_filter, null)
        setContentView(contentView)

        btFilter = contentView.findViewById(R.id.btFilter)
        tagEdit = contentView.findViewById(R.id.tag_edit)


    }

    fun setCallback(cb: Callback) {
        mCallback = cb
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.btFilter -> {
                if (mCallback != null) {
                    postFilterEntity.title = btFilter!!.text.toString()
                    postFilterEntity.tag = tagEdit!!.text.toString()
                    mCallback!!.confirm(postFilterEntity)
                }
                dismiss()
            }
            R.id.maskView -> dismiss()
        }
    }


    interface Callback {

        fun confirm(data: NoteFilterData)
    }


}
