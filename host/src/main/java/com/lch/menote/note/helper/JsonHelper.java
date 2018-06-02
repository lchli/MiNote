package com.lch.menote.note.helper;

import com.google.gson.reflect.TypeToken;
import com.lch.menote.note.domain.MusicData;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2017/9/28.
 */

public class JsonHelper {

    public static Type getMusicListTypeToken() {
        return new TypeToken<List<MusicData>>() {
        }.getType();
    }
}
