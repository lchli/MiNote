package com.lch.menote;

/**
 * Created by lichenghang on 2018/5/27.
 */

public final class ApiConstants {
    public static final String APP_KEY="lch-minote-#@#";

    public static final int RESPCODE_SUCCESS = 1;
    public static final int RESPCODE_FAILE = 2;

    public static final String IP = "192.168.1.101";
    public static final String PORT = "8088";
    public static final String HOST = "http://" + IP + ":" + PORT;

    public static final String REGISTER = HOST + "/api/sec/user/register";
    public static final String APK_UPDATE = HOST + "/api/public/apk/update";
    public static final String USER_UPDATE = HOST + "/api/sec/user/update";
    public static final String LOGIN = HOST + "/api/sec/user/login";
    public static final String USER_GET_BY_ID = HOST + "/api/sec/user/findById";
    public static final String UPLOAD_NOTE = HOST + "/api/sec/note/save";
    public static final String LIKE_NOTE = HOST + "/api/sec/note/like";
    public static final String DELETE_NOTE = HOST + "/api/sec/note/delete";
    public static final String QUERY_NOTE = HOST + "/api/sec/note/get";
    public static final String UPLOAD_FILE = HOST + "/api/sec/file/upload";




}
