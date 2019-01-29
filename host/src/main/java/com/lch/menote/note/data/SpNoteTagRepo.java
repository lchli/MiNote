package com.lch.menote.note.data;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.lch.menote.note.datainterface.NoteTagSource;
import com.lch.menote.note.route.NoteRouteApiImpl;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.utils.tool.AliJsonHelper;

import java.util.ArrayList;
import java.util.List;

public class SpNoteTagRepo implements NoteTagSource {
    private static final String KEY_NOTE_TAGS = "KEY_NOTE_TAGS";

    @Override
    public ResponseValue<Void> addTag(String tag) {
        ResponseValue<Void> res = new ResponseValue<>();

        try {
            String json = SPUtils.getInstance(NoteRouteApiImpl.SP).getString(KEY_NOTE_TAGS);
            List<String> tags = AliJsonHelper.parseArray(json, String.class);
            if (tags == null) {
                tags = new ArrayList<>();
            }
            if (tags.contains(tag)) {
                res.setErrorMsg("标签已经存在");
                return res;
            }
            tags.add(tag);

            json = AliJsonHelper.toJSONString(tags);

            SPUtils.getInstance(NoteRouteApiImpl.SP).put(KEY_NOTE_TAGS, json);
        } catch (Throwable e) {
            e.printStackTrace();
            res.setErrorMsg(e.getMessage());
        }

        return res;
    }

    @Override
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
            res.setErrorMsg(e.getMessage());
        }

        return res;
    }

    @Override
    public ResponseValue<List<String>> getAllTag() {
        ResponseValue<List<String>> res = new ResponseValue<>();

        try {
            String json = SPUtils.getInstance(NoteRouteApiImpl.SP).getString(KEY_NOTE_TAGS);
            if (TextUtils.isEmpty(json)) {
                return res;
            }

            res.data = AliJsonHelper.parseArray(json, String.class);

        } catch (Throwable e) {
            e.printStackTrace();
            res.setErrorMsg(e.getMessage());
        }
        return res;
    }
}
