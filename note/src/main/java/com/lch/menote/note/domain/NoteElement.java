package com.lch.menote.note.domain;

import java.io.Serializable;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class NoteElement implements Serializable{
    public static final String TYPE_TEXT="text";
    public static final String TYPE_IMG="img";

    public String type=TYPE_TEXT;
    public String text;
    public String path;

    public transient boolean isSelected=false;

}
