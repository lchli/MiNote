package com.lch.menote.note.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.text.Html
import android.widget.TextView

import com.lch.menote.common.util.BitmapScaleUtil
import com.lch.menote.common.util.HttpHelper
import com.lch.menote.common.util.UiHandler
import com.lch.menote.note.helper.BITMAP_MAX_MEMORY
import com.lch.menote.note.helper.STUDY_APP_ROOT_DIR


class URLImageGetter(private val textView: TextView) : Html.ImageGetter {

    private val context: Context = textView.context

    override fun getDrawable(src: String): Drawable {
        val urlDrawable = URLDrawable(context, src)

        val getterTask = ImageGetterAsyncTask(urlDrawable)
        getterTask.execute(src)
        return urlDrawable
    }

    private inner class ImageGetterAsyncTask(internal var urlDrawable: URLDrawable) : AsyncTask<String, Void, Drawable>() {

        override fun onPostExecute(result: Drawable?) {
            if (result != null) {
                urlDrawable.setDrawable(result)
                UiHandler.post { textView.invalidate() }

            }
        }

        override fun doInBackground(vararg params: String): Drawable? {
            val source = params[0]
            val bmp: Bitmap?
            bmp = if (isNetImagePath(source)) {
                BitmapScaleUtil.decodeSampledBitmapFromUrl(HttpHelper.addExtraParamsToUrl(source,null),BITMAP_MAX_MEMORY);
            } else {
                BitmapScaleUtil.decodeSampledBitmapFromPath(STUDY_APP_ROOT_DIR + source, BITMAP_MAX_MEMORY)
            }
            return if (bmp == null) {
                null
            } else BitmapDrawable(bmp)
        }


    }

    private fun isNetImagePath(path: String?): Boolean {
        return if (path == null) {
            false
        } else path.startsWith("http://") || path.startsWith("https://")
    }

}