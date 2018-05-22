package com.lch.menote.note.helper;

import android.os.Environment;

/**
 * Created by Administrator on 2017/9/22.
 */

public class ConstantUtil {
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String STUDY_APP_ROOT_DIR = String.format("%s/StudyApp",SD_PATH);
    public static final String NOTE_DB = "note.db";
    public static final String DB_DIR = String.format("%s/database",STUDY_APP_ROOT_DIR);

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_PINED = 2;
    public static final long BITMAP_MAX_MEMORY = 5 * 1024 * 1024L;

}