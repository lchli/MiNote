package com.lch.menote;

/**
 * Created by lichenghang on 2018/5/27.
 */

public final class ApiConstants {

    public static final int RESPCODE_SUCCESS = 1;
    public static final int RESPCODE_FAILE = 2;

    public static final String IP = "192.168.1.101";
    public static final String PORT = "8088";
    public static final String HOST = "http://" + IP + ":" + PORT;

    public static final String REGISTER = HOST + "/user/register";
    public static final String USER_UPDATE = HOST + "/user/update";
    public static final String LOGIN = HOST + "/user/login";
    public static final String USER_GET_BY_ID = HOST + "/user/findById";
    public static final String UPLOAD_NOTE = HOST + "/note/save";
    public static final String QUERY_NOTE = HOST + "/note/get";
    public static final String UPLOAD_FILE = HOST + "/file/upload";


}
