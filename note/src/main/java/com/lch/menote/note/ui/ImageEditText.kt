package com.lch.menote.note.ui

import android.content.Context
import android.os.AsyncTask
import android.text.Html
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import com.lch.menote.common.util.BitmapScaleUtil
import com.lch.menote.note.helper.BITMAP_MAX_MEMORY
import com.lch.menote.note.helper.NoteUtils
import java.io.File
import java.io.IOException

/**
 * Created by lchli on 2016/8/12.
 */

class ImageEditText : EditText {


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun setText(text: CharSequence, type: TextView.BufferType) {
        super.setText(text, type)
        setSelection(getText().length)
    }

    fun insertImage(imagePath: String, imageGetter: Html.ImageGetter, courseDir: String, noteUid: String) {
        val sourceImage = File(imagePath)
        if (!sourceImage.exists()) {
            return
        }
        object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void): String? {
                try {
                    val bmp = BitmapScaleUtil.decodeSampledBitmapFromPath(imagePath, BITMAP_MAX_MEMORY)
                    val imageName = NoteUtils.buildNoteImageName(bmp!!.width, bmp.height)
                    val destFile = File(courseDir, imageName)
                    if (!destFile.exists()) {
                        try {
                            destFile.createNewFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                            // ToastUtils.systemToast(R.string.insert_image_fail);
                            return null
                        }

                    }
                    BitmapScaleUtil.saveBitmap(bmp, destFile, 100)
                    bmp.recycle()
                    return imageName
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                }

            }

            override fun onPostExecute(imageName: String?) {
                if (imageName != null) {
                    val imgLabel = NoteUtils.buildImgLabel(imageName, noteUid)
                    var selection = selectionStart
                    if (selection == -1) {
                        selection = 0
                    }
                    text.insert(selection, Html.fromHtml(imgLabel, imageGetter, null))
                } else {
                    //ToastUtils.systemToast(R.string.insert_image_fail);
                }
            }
        }.execute()

    }


}
