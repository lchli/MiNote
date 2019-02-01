package com.lch.menote.note.data;

import android.support.annotation.NonNull;

import com.apkfuns.logutils.LogUtils;
import com.lch.menote.ApiConstants;
import com.lch.menote.model.BaseResponse;
import com.lch.menote.note.data.entity.Note;
import com.lch.menote.note.data.response.QueryNoteResponse;
import com.lch.menote.note.data.response.SingleNoteResponse;
import com.lch.menote.note.datainterface.RemoteNoteSource;
import com.lch.menote.note.model.CloudNoteModel;
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
    public ResponseValue<List<CloudNoteModel>> queryNotes(NetNoteQuery query) {

        ApiRequestParams param = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.QUERY_NOTE)
                .addParam("ownerUserId", query.userId)
                .addParam("page", query.page + "")
                .addParam("uid", query.uid)
                .addParam("pageSize", query.pageSize + "")
                .addParam("title", query.title)
                .addParam("type", query.tag)
                .addParam("sort", query.sortResult());


        NetworkResponse<QueryNoteResponse> ret = NetKit.apiRequest().syncPost(param, new Parser<QueryNoteResponse>() {
            @Override
            public QueryNoteResponse parse(String s) {
                LogUtils.e(s);
                return AliJsonHelper.parseObject(s, QueryNoteResponse.class);
            }
        });

        if (ret.hasError()) {
            return new ResponseValue<List<CloudNoteModel>>().setErrorMsg(ret.getErrorMsg());
        }

        if (ret.data == null) {
            return new ResponseValue<List<CloudNoteModel>>().setErrorMsg("ret data is null.");
        }


        return new ResponseValue<List<CloudNoteModel>>().setData(ret.data.data);
    }


    @Override
    public ResponseValue<Void> upload(@NotNull Note note) {
        ResponseValue<Void> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.UPLOAD_NOTE)
                .addParam("content", note.content)
                .addParam("title", note.title)
                .addParam("type", note.type)
                .addParam("uid", note.uid)
                .addParam("isPublic", CloudNoteModel.PUBLIC_FALSE);


        NetworkResponse<BaseResponse> res = NetKit.apiRequest().syncPost(params, new Parser<BaseResponse>() {
            @Override
            public BaseResponse parse(String s) {
                LogUtils.e(s);
                return AliJsonHelper.parseObject(s, BaseResponse.class);
            }
        });


        if (res.hasError()) {
            LogUtils.e("hasError:" + res.getErrorMsg());
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
                LogUtils.e(s);
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
    public ResponseValue<CloudNoteModel> likeNote(String noteId) {
        ResponseValue<CloudNoteModel> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.LIKE_NOTE)
                .addParam("noteId", noteId);

        NetworkResponse<SingleNoteResponse> res = NetKit.apiRequest().syncPost(params, new Parser<SingleNoteResponse>() {
            @Override
            public SingleNoteResponse parse(String s) {
                LogUtils.e(s);
                return AliJsonHelper.parseObject(s, SingleNoteResponse.class);
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

        ret.setData(res.data.data);

        return ret;

    }

    @Override
    public ResponseValue<CloudNoteModel> publicNote(String noteId) {
        ResponseValue<CloudNoteModel> ret = new ResponseValue<>();

        ApiRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.PUBLIC_NOTE)
                .addParam("noteId", noteId);

        NetworkResponse<SingleNoteResponse> res = NetKit.apiRequest().syncPost(params, new Parser<SingleNoteResponse>() {
            @Override
            public SingleNoteResponse parse(String s) {
                LogUtils.e(s);
                return AliJsonHelper.parseObject(s, SingleNoteResponse.class);
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

        ret.setData(res.data.data);

        return ret;
    }
}
