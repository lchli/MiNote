package com.lch.menote.note.domain;

import java.io.Serializable;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class NoteModel implements Serializable {

    public static final int CAT_NOTE = 1;
    public static final int CAT_MUSIC = 2;

    private static final long serialVersionUID = -5367845055852303764L;
    public String imagesDir = "";

    public String content = "";

    public String lastModifyTime;

    public String title = "";

    public String type = "";

    public String thumbNail = "";

    public String uid;

    public String userId;

    public String ShareUrl = "";

    public int category = CAT_NOTE;
}
