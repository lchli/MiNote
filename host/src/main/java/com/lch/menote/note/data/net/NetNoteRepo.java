package com.lch.menote.note.data.net;

import android.support.annotation.NonNull;

import com.lch.menote.ApiConstants;
import com.lch.menote.ConstantUtil;
import com.lch.menote.note.data.NoteSource;
import com.lch.menote.note.domain.BaseResponse;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.note.domain.QueryNoteResponse;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.string.Parser;
import com.lch.netkit.string.StringRequestParams;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class NetNoteRepo implements NoteSource {

    @NonNull
    public ResponseValue<QueryNoteResponse> queryNotes(@Nullable String tag, @Nullable String title, boolean sortTimeAsc, @NotNull String useId) {
        StringRequestParams param = new StringRequestParams()
                .setUrl(ApiConstants.QUERY_NOTE)
                .addParam("UserId", useId)
                .addParam("UserToken", "");


        ResponseValue<QueryNoteResponse> ret = NetKit.stringRequest().postSync(param, new Parser<QueryNoteResponse>() {
            @Override
            public QueryNoteResponse parse(String s) {
                return AliJsonHelper.parseObject(s, QueryNoteResponse.class);
            }
        });

        return ret;
    }

    public ResponseValue<Void> save(@NotNull NoteModel note) {
        ResponseValue<Void> ret = new ResponseValue<>();

        StringRequestParams params = new StringRequestParams()
                .setUrl(ApiConstants.UPLOAD_NOTE)
                .addParam("content", note.content)
                .addParam("title", note.title)
                .addParam("type", note.type)
                .addParam("thumbNail", note.thumbNail)
                .addParam("uid", note.uid)
                .addParam("userId", note.userId);

        ResponseValue<BaseResponse> res = NetKit.stringRequest().postSync(params, new Parser<BaseResponse>() {
            @Override
            public BaseResponse parse(String s) {
                return AliJsonHelper.parseObject(s, BaseResponse.class);
            }
        });

        if (res.hasError()) {
            ret.err = res.err;
            return ret;
        }

        if (res.data == null) {
            ret.setErrMsg("ret data is null");
            return ret;
        }

        if (res.data.status != ConstantUtil.SERVER_REQUEST_SUCCESS) {
            ret.setErrMsg(res.data.message);
            return ret;
        }

        return ret;

    }

    public ResponseValue<Void> delete(@NotNull Note note) {

        return null;
    }
}
