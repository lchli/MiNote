package com.lch.menote.note.helper

import android.os.Environment

/**
 * Created by Administrator on 2017/9/22.
 */
internal val SD_PATH = Environment.getExternalStorageDirectory().absolutePath
internal val STUDY_APP_ROOT_DIR = "$SD_PATH/StudyApp"
internal const val NOTE_DB = "notes.db"

internal const val VIEW_TYPE_HEADER = 0
internal const val VIEW_TYPE_ITEM = 1
internal const val VIEW_TYPE_PINED = 2
internal const val BITMAP_MAX_MEMORY = 5 * 1024 * 1024L