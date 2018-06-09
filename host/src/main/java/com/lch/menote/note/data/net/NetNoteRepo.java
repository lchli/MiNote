package com.lch.menote.note.data.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lch.menote.ApiConstants;
import com.lch.menote.ConstantUtil;
import com.lch.menote.note.domain.BaseResponse;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.note.domain.QueryNoteResponse;
import com.lch.menote.note.route.RouteCall;
import com.lch.menote.user.route.User;
import com.lch.menote.user.route.UserRouteApi;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
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
        private String sortKey;
        private String sortDiretion;
        private String tag;
        private String title;
        private String useId;
        private String uid;
        private String token;
        private boolean isPublic = false;
        private int page;
        private int pageSize;

        public static NetNoteQuery newInstance() {
            return new NetNoteQuery();
        }

        public NetNoteQuery setSortDiretion(String sortDiretion) {
            this.sortDiretion = sortDiretion;
            return this;
        }

        public NetNoteQuery setSortKey(String sortKey) {
            this.sortKey = sortKey;
            return this;
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
            isPublic = aPublic;
            return this;
        }

        public NetNoteQuery setUid(String uid) {
            this.uid = uid;
            return this;
        }
    }

    @NonNull
    public ResponseValue<QueryNoteResponse> queryNotes(NetNoteQuery query) {
        JSONArray jsonArray = new JSONArray();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", query.sortKey);
            jsonObject.put("direction", query.sortDiretion);

            jsonArray.put(jsonObject);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        StringRequestParams param = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.QUERY_NOTE)
                .addParam("userId", query.useId)
                .addParam("page", query.page + "")
                .addParam("uid", query.uid)
                .addParam("pageSize", query.pageSize + "")
                .addParam("isPublic", query.isPublic + "")
                .addParam("sort", jsonArray.toString())
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

        String useid = null;
        UserRouteApi userMod = RouteCall.getUserModule();
        if (userMod != null) {
            User se = userMod.userSession();
            if (se != null) {
                useid = se.uid;
            }
        }

        if (TextUtils.isEmpty(useid)) {
            ret.setErrMsg("user not login");
            return ret;
        }

        StringRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.UPLOAD_NOTE)
                .addParam("content", note.content)
                .addParam("title", note.title)
                .addParam("type", note.type)
                .addParam("thumbNail", note.thumbNail)
                .addParam("uid", note.uid)
                .addParam("isPublic", note.isPublic + "")
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

        if (res.data.status != ConstantUtil.SERVER_REQUEST_SUCCESS) {
            ret.setErrMsg(res.data.message);
            return ret;
        }

        return ret;

    }

    public ResponseValue<Void> delete(@NotNull Note note) {

        return null;
    }


    public ResponseValue<Void> likeNote(String noteId) {
        ResponseValue<Void> ret = new ResponseValue<>();

        String useid = null;
        String userToken = null;
        UserRouteApi userMod = RouteCall.getUserModule();
        if (userMod != null) {
            User se = userMod.userSession();
            if (se != null) {
                useid = se.uid;
                userToken=se.token;
            }
        }

        if (TextUtils.isEmpty(useid)||TextUtils.isEmpty(userToken)) {
            ret.setErrMsg("user not login");
            return ret;
        }

        StringRequestParams params = RequestUtils.minoteStringRequestParams()
                .setUrl(ApiConstants.LIKE_NOTE)
                .addParam("noteId", noteId)
                .addParam("userToken",userToken)
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

        if (res.data.status != ConstantUtil.SERVER_REQUEST_SUCCESS) {
            ret.setErrMsg(res.data.message);
            return ret;
        }

        return ret;

    }
}
