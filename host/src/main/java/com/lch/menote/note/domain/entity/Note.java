package com.lch.menote.note.domain.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/21.
 */
@Entity
public class Note implements Serializable {

    public static final int CAT_NOTE = 1;
    public static final int CAT_MUSIC = 2;

    private static final long serialVersionUID = -5367845055852303764L;
    public String imagesDir = "";

    public String content = "";

    public long lastModifyTime;

    public String title = "";

    public String type = "";

    public String thumbNail = "";

    @Id
    public String uid;

    public String userId;

    public int category = CAT_NOTE;

    @Generated(hash = 22410477)
    public Note(String imagesDir, String content, long lastModifyTime, String title,
            String type, String thumbNail, String uid, String userId,
            int category) {
        this.imagesDir = imagesDir;
        this.content = content;
        this.lastModifyTime = lastModifyTime;
        this.title = title;
        this.type = type;
        this.thumbNail = thumbNail;
        this.uid = uid;
        this.userId = userId;
        this.category = category;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }

    public String getImagesDir() {
        return this.imagesDir;
    }

    public void setImagesDir(String imagesDir) {
        this.imagesDir = imagesDir;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbNail() {
        return this.thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

}
