package com.lch.menote.note.data.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lch.menote.ApiConstants;
import com.lch.menote.R;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.model.BaseResponse;
import com.lch.menote.note.domain.response.QueryNoteResponse;
import com.lch.menote.user.data.sp.SpUserRepo;
import com.lch.menote.user.route.User;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.common.tool.ContextProvider;
import com.lch.netkit.string.Parser;
import com.lch.netkit.string.StringRequestParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class NetNoteRepo {

    public static class NetNoteQuery {
        public static final String DIRECTION_ASC = "asc";
        public static final String DIRECTION_DESC = "desc";

        private String tag;
        private String title;
        private String useId;
        private String uid;
        private String token;
        private String isPublic;
        private int page = 0;
        private int pageSize = 20;

        private JSONArray jsonArray = new JSONArray();

        public static NetNoteQuery newInstance() {
            return new NetNoteQuery();
        }

        public NetNoteQuery setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public NetNoteQuery setTitle(String title) {
            this.title = title;
            return this;
        }

        public NetNoteQuery setUseId(String useId) {
            this.useId = useId;
            return this;
        }

        public NetNoteQuery setPage(int page) {
            this.page = page;
            return this;
        }

        public NetNoteQuery setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public NetNoteQuery setPublic(boolean aPublic) {
            isPublic = aPublic ? "1" : "0";
            return this;
        }

        public NetNoteQuery setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public NetNoteQuery addSort(String key, String sortDiretion) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("key", key);
                jsonObject.put("direction", sortDiretion);

                jsonArray.put(jsonObject);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return this;
        }


        public String sortResult() {
            return jsonArray.toString();
        }
    }

    @NonNull
    public ResponseValue<QueryNoteResponse> queryNotes(NetNoteQuery query) {

        StringRequestParams param = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.QUERY_NOTE)
                .addParam("userId", query.useId)
                .addParam("page", query.page + "")
                .addParam("uid", query.uid)
                .addParam("pageSize", query.pageSize + "")
                .addParam("isPublic", query.isPublic)
                .addParam("sort", query.sortResult())
                .addParam("userToken", query.token);


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

        User se = SpUserRepo.getINS().getUser().data;

        String useid = null;
        String userToken = null;

        if (se != null) {
            useid = se.uid;
            userToken=se.token;
        }


        if (TextUtils.isEmpty(useid)) {
            ret.setErrMsg(ContextProvider.context().getString(R.string.not_login));
            return ret;
        }

        StringRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.UPLOAD_NOTE)
                .addParam("content", note.content)
                .addParam("title", note.title)
                .addParam("type", note.type)
                .addParam("thumbNail", note.thumbNail)
                .addParam("uid", note.uid)
                .addParam("isPublic", note.isPublic)
                .addParam("userToken", userToken)
                .addParam("userId", useid);

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

        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrMsg(res.data.message);
            return ret;
        }

        return ret;

    }

    public ResponseValue<Void> delete(@NotNull String noteId) {
        ResponseValue<Void> ret = new ResponseValue<>();

        User se = SpUserRepo.getINS().getUser().data;

        String useid = null;
        String userToken = null;

        if (se != null) {
            useid = se.uid;
            userToken = se.token;
        }

        if (TextUtils.isEmpty(useid) || TextUtils.isEmpty(userToken)) {
            ret.setErrMsg(ContextProvider.context().getString(R.string.not_login));
            return ret;
        }

        StringRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.DELETE_NOTE)
                .addParam("noteId", noteId)
                .addParam("userToken", userToken)
                .addParam("userId", useid);

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

        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrMsg(res.data.message);
            return ret;
        }

        return ret;
    }


    public ResponseValue<Void> likeNote(String noteId) {
        ResponseValue<Void> ret = new ResponseValue<>();

        User se = SpUserRepo.getINS().getUser().data;

        String useid = null;
        String userToken = null;

        if (se != null) {
            useid = se.uid;
            userToken = se.token;
        }


        if (TextUtils.isEmpty(useid) || TextUtils.isEmpty(userToken)) {
            ret.setErrMsg(ContextProvider.context().getString(R.string.not_login));
            return ret;
        }

        StringRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.LIKE_NOTE)
                .addParam("noteId", noteId)
                .addParam("userToken", userToken)
                .addParam("userId", useid);

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

        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
            ret.setErrMsg(res.data.message);
            return ret;
        }

        return ret;

    }
}
