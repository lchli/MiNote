package com.lch.menote.note.data;

import android.support.annotation.NonNull;

import com.lch.menote.ApiConstants;
import com.lch.menote.model.BaseResponse;
import com.lch.menote.note.datainterface.RemoteNoteSource;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.data.response.QueryNoteResponse;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.v2.NetKit;
import com.lch.netkit.v2.apirequest.ApiRequestParams;
import com.lch.netkit.v2.common.NetworkResponse;
import com.lch.netkit.v2.parser.Parser;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.utils.tool.AliJsonHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class NetNoteRepo implements RemoteNoteSource {


    @Override
    @NonNull
    public ResponseValue<List<NoteModel>> queryNotes(NetNoteQuery query) {

        ApiRequestParams param = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.QUERY_NOTE)
                .addParam("userId", query.userId)
                .addParam("page", query.page + "")
                .addParam("uid", query.uid)
                .addParam("pageSize", query.pageSize + "")
                .addParam("isPublic", query.isPublic)
                .addParam("sort", query.sortResult());


        NetworkResponse<QueryNoteResponse> ret = NetKit.apiRequest().syncPost(param, new Parser<QueryNoteResponse>() {
            @Override
            public QueryNoteResponse parse(String s) {
                return AliJsonHelper.parseObject(s, QueryNoteResponse.class);
            }
        });

        if (ret.hasError()) {
            return new ResponseValue<List<NoteModel>>().setErrorMsg(ret.getErrorMsg());
        }

        if (ret.data == null) {
            return new ResponseValue<List<NoteModel>>().setErrorMsg("ret data is null.");
        }


        return new ResponseValue<List<NoteModel>>().setData(ret.data.data);
    }


    @Override
    public ResponseValue<Void> save(@NotNull NoteModel note) {
        ResponseValue<Void> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.UPLOAD_NOTE)
                .addParam("content", note.content)
                .addParam("title", note.title)
                .addParam("type", note.type)
                .addParam("thumbNail", note.thumbNail)
                .addParam("uid", note.uid)
                .addParam("isPublic", note.isPublic);


        NetworkResponse<BaseResponse> res = NetKit.apiRequest().syncPost(params, new Parser<BaseResponse>() {
            @Override
            public BaseResponse parse(String s) {
                return AliJsonHelper.parseObject(s, BaseResponse.class);
            }
        });


        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null) {
            ret.setErrorMsg("ret data is null");
            return ret;
        }

        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrorMsg(res.data.message);
            return ret;
        }

        return ret;

    }


    @Override
    public ResponseValue<Void> delete(@NotNull String noteId) {
        ResponseValue<Void> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.DELETE_NOTE)
                .addParam("noteId", noteId);


        NetworkResponse<BaseResponse> res = NetKit.apiRequest().syncPost(params, new Parser<BaseResponse>() {
            @Override
            public BaseResponse parse(String s) {
                return AliJsonHelper.parseObject(s, BaseResponse.class);
            }
        });

        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null) {
            ret.setErrorMsg("ret data is null");
            return ret;
        }

        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrorMsg(res.data.message);
            return ret;
        }

        return ret;
    }


    @Override
    public ResponseValue<Void> likeNote(String noteId) {
        ResponseValue<Void> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.LIKE_NOTE)
                .addParam("noteId", noteId);

        NetworkResponse<BaseResponse> res = NetKit.apiRequest().syncPost(params, new Parser<BaseResponse>() {
            @Override
            public BaseResponse parse(String s) {
                return AliJsonHelper.parseObject(s, BaseResponse.class);
            }
        });

        if (res.hasError()) {
            ret.setErrorMsg(res.getErrorMsg());
            return ret;
        }

        if (res.data == null) {
            ret.setErrorMsg("ret data is null");
            return ret;
        }

        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrorMsg(res.data.message);
            return ret;
        }

        return ret;

    }
}
