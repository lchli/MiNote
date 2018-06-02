package com.lch.menote.note.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.text.Html
import android.widget.TextView

import com.lch.menote.note.helper.ConstantUtil
import com.lch.netkit.common.tool.BitmapScaleUtil
import com.lch.netkit.common.tool.HttpHelper
import com.lch.netkit.common.tool.UiHandler


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
                BitmapScaleUtil.decodeSampledBitmapFromUrl(HttpHelper.addExtraParamsToUrl(source,null), ConstantUtil.BITMAP_MAX_MEMORY);
            } else {
                BitmapScaleUtil.decodeSampledBitmapFromPath(ConstantUtil.STUDY_APP_ROOT_DIR + source, ConstantUtil.BITMAP_MAX_MEMORY)
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