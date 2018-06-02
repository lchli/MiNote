package com.lch.menote.note.data.sp;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.lch.menote.note.route.NoteRouteApiImpl;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;

import java.util.ArrayList;
import java.util.List;

public class SpNoteTagRepo {
    private static final String KEY_NOTE_TAGS = "KEY_NOTE_TAGS";

    public ResponseValue<Void> addTag(String tag) {
        ResponseValue<Void> res = new ResponseValue<>();
        try {
            String json = SPUtils.getInstance(NoteRouteApiImpl.SP).getString(KEY_NOTE_TAGS);
            List<String> tags = AliJsonHelper.parseArray(json, String.class);
            if (tags == null) {
                tags = new ArrayList<>();
            }
            if (tags.contains(tag)) {
                res.setErrMsg("标签已经存在");
                return res;
            }
            tags.add(tag);

            json = AliJsonHelper.toJSONString(tags);

            SPUtils.getInstance(NoteRouteApiImpl.SP).put(KEY_NOTE_TAGS, json);
        } catch (Throwable e) {
            e.printStackTrace();
            res.setErrMsg(e.getMessage());
        }

        return res;
    }

    public ResponseValue<Void> removeTag(String tag) {
        ResponseValue<Void> res = new ResponseValue<>();

        try {
            String json = SPUtils.getInstance(NoteRouteApiImpl.SP).getString(KEY_NOTE_TAGS);
            List<String> tags = AliJsonHelper.parseArray(json, String.class);
            if (tags == null) {
                return res;
            }
            tags.remove(tag);

            json = AliJsonHelper.toJSONString(tags);
            SPUtils.getInstance(NoteRouteApiImpl.SP).put(KEY_NOTE_TAGS, json);
        } catch (Throwable e) {
            e.printStackTrace();
            res.setErrMsg(e.getMessage());
        }

        return res;
    }

    public ResponseValue<List<String>> getAllTag() {
        ResponseValue<List<String>> res = new ResponseValue<>();

        try {
            String json = SPUtils.getInstance(NoteRouteApiImpl.SP).getString(KEY_NOTE_TAGS);
            if (TextUtils.isEmpty(json)) {
                return res;
            }
            List<String> tags = AliJsonHelper.parseArray(json, String.class);
            res.data = tags;

        } catch (Throwable e) {
            e.printStackTrace();
            res.setErrMsg(e.getMessage());
        }
        return res;
    }
}
