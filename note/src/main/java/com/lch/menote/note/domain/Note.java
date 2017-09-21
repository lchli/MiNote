package com.lch.menote.note.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/9/21.
 */
@Entity(nameInDb = "note_table")
public class Note {

    private String title;

    @Generated(hash = 811351793)
    public Note(String title) {
        this.title = title;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

  
}
