package com.lch.menote.note.model;

import java.io.Serializable;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class NoteElement implements Serializable{
    public static final String TYPE_TEXT="text";
    public static final String TYPE_IMG="img";
    public static final String TYPE_AUDIO="audio";
    public static final String TYPE_VIDEO="video";
    private static final long serialVersionUID = -5420387017972056427L;

    public String type=TYPE_TEXT;
    public String text;
    public String path;

    public transient boolean isSelected=false;

}
