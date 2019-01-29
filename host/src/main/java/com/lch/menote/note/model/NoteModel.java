package com.lch.menote.note.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class NoteModel implements Serializable {

    public static final int CAT_NOTE = 1;
    public static final int CAT_MUSIC = 2;
    public static final String PUBLIC_TRUE = "1";
    public static final String PUBLIC_FALSE = "0";

    private static final long serialVersionUID = -5367845055852303764L;
    public String imagesDir = "";

    public String content = "";


    public long updateTime;

    public String title = "";

    public String type = "";

    public String thumbNail = "";

    public String uid;

    public String userId;
    public String userHeadUrl;
    public String userName;
    public String isPublic=PUBLIC_FALSE;

    public String ShareUrl = "";

    public int category = CAT_NOTE;
    public List<String> star;

    public boolean isPublic(){
        return PUBLIC_TRUE.equals(isPublic);
    }
}
