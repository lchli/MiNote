package com.lch.menote.note.helper

import android.text.Spannable
import android.text.style.ImageSpan
import android.widget.TextView
import com.apkfuns.logutils.LogUtils
import com.lch.menote.common.util.UUIDUtils
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.*


/**
 * Created by lchli on 2016/8/13.
 */

object NoteUtils {

    private val IMAGE_WIDTH_HEIGHT_DEVIDER = "x"
    private val IMAGE_NAME_DEVIDER = "_"
    private val IMAGE_NAME_PATTERN =
            "%s$IMAGE_WIDTH_HEIGHT_DEVIDER%s$IMAGE_NAME_DEVIDER%s.jpg"// 300x200_test.jpg


    private val HTML_IMG_PATTERN = "<br><img src=\"/resources/%s/%s\" /><br>"

    private val NOTE_IMAGES_DIR = "$STUDY_APP_ROOT_DIR/resources"

    init {
        try {
            FileUtils.forceMkdir(File(NOTE_IMAGES_DIR))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    /**
     * 从TextView的Html内容里面查找img标签的src属性值。例如：<img src=...></img>
     *
     * @param textView
     * @param srcPrefix 需要向src值添加的前缀路径。
     * @return
     */
    fun parseImageSpanSrc(textView: TextView, srcPrefix: String): ArrayList<String> {
        val sp = textView.text as Spannable
        val spans = sp.getSpans(0, sp.length, ImageSpan::class.java)
        val imgSrcs = ArrayList<String>()
        for (i in spans.indices) {
            val span = spans[i]
            if (span is ImageSpan) {
                val imgSrc = span.source
                imgSrcs.add(srcPrefix + imgSrc)
                LogUtils.e("imgsrc:" + imgSrc)
            }
        }
        return imgSrcs

    }

    /**
     * 从图片的名字里面解析出图片的宽高信息。如：300x200_test.jpg
     *
     * @param imageName
     * @return 包含宽、高的数组。
     */
    fun parseNoteImageSize(imageName: String): IntArray {
        try {
            val start = imageName.lastIndexOf("/")
            val index = imageName.lastIndexOf(IMAGE_NAME_DEVIDER)
            val wh = imageName.substring(start + 1, index).split(IMAGE_WIDTH_HEIGHT_DEVIDER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val w = Integer.parseInt(wh[0])
            val h = Integer.parseInt(wh[1])
            return intArrayOf(w, h)
        } catch (e: Exception) {
            e.printStackTrace()
            return intArrayOf(200, 200)
        }

    }

    /**
     * 生成图片的名字，其中包含宽、高信息。如：300x200_test.jpg
     *
     * @param width
     * @param height
     * @return
     */
    fun buildNoteImageName(width: Int, height: Int): String {
        return String.format(IMAGE_NAME_PATTERN, width, height, UUIDUtils.uuid())
    }


    fun buildImgLabel(imgName: String?, noteUid: String): String? {
        return if (imgName == null) {
            null
        } else String.format(Locale.ENGLISH, HTML_IMG_PATTERN, noteUid, imgName)
    }

    fun buildNoteDir(courseUUID: String): String {
        return String.format("%s/%s/", NOTE_IMAGES_DIR, courseUUID)
    }
}
