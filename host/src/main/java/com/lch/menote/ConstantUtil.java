package com.lch.menote;

import android.os.Environment;

/**
 * Created by Administrator on 2017/9/22.
 */

public final class ConstantUtil {
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String STUDY_APP_ROOT_DIR = String.format("%s/StudyApp", SD_PATH);
    public static final String NOTE_DB = "note.db";
    public static final String DB_DIR = String.format("%s/database", STUDY_APP_ROOT_DIR);

    public static final long BITMAP_MAX_MEMORY = 5 * 1024 * 1024L;

}