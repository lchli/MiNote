package com.lch.menote.note.helper

import com.lch.menote.ConstantUtil
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException


/**
 * Created by lchli on 2016/8/13.
 */

object NoteUtils {

    private val NOTE_IMAGES_DIR = "${ConstantUtil.STUDY_APP_ROOT_DIR}/resources"

    init {
        try {
            FileUtils.forceMkdir(File(NOTE_IMAGES_DIR))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }



    fun buildNoteDir(courseUUID: String): String {
        return String.format("%s/%s/", NOTE_IMAGES_DIR, courseUUID)
    }
}
