package com.lch.menote.user;

/**
 * Created by lichenghang on 2018/5/27.
 */

public final class ServerRequestCode {

    public static final int RESPCODE_SUCCESS = 1;
    public static final int RESPCODE_FAILE = 2;

    public static final String IP = "192.168.1.101";
    public static final String PORT = "8088";
    public static final String HOST = "http://" + IP + ":" + PORT;
    public static final String REGISTER = HOST + "/user/register";
    public static final String LOGIN = HOST + "/user/login";


}
