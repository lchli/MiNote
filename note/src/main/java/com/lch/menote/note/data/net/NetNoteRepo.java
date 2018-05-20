package com.lch.menote.note.data.net;

import android.support.annotation.NonNull;

import com.lch.menote.common.util.AliJsonHelper;
import com.lch.menote.note.data.NoteSource;
import com.lch.menote.note.domain.Note;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.string.Parser;
import com.lch.netkit.string.StringRequestParams;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class NetNoteRepo implements NoteSource {

    @NonNull
    @Override
    public ResponseValue<List<Note>> queryNotes(@Nullable String tag, @Nullable String title, boolean sortTimeAsc, @NotNull String useId) {
        StringRequestParams param= new StringRequestParams()
                .addParam("UserId",useId)
                .addParam("UserToken","");


        ResponseValue<List<Note>> ret = NetKit.stringRequest().postSync(param, new Parser<List<Note>>() {
            @Override
            public List<Note> parse(String s) {
                return AliJsonHelper.parseArray(s,Note.class);
            }
        });

        return ret;
    }

    @Override
    public void save(@NotNull Note note) {

    }

    @Override
    public void delete(@NotNull Note note) {

    }
}
