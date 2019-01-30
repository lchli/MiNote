package com.lch.menote.note.datainterface;

import com.lch.menote.note.model.NoteModel;
import com.lchli.arch.clean.ResponseValue;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2019/1/29.
 */

public interface RemoteNoteSource {

    class NetNoteQuery {
        public static final String DIRECTION_ASC = "asc";
        public static final String DIRECTION_DESC = "desc";

        public String tag;
        public String title;
        public String uid;
        public String userId;
        public String isPublic;
        public int page = 0;
        public int pageSize = 20;

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

        public NetNoteQuery setUserId(String userId) {
            this.userId = userId;
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

    ResponseValue<List<NoteModel>> queryNotes(NetNoteQuery query);

    ResponseValue<Void> save(@NotNull NoteModel note);

    ResponseValue<Void> delete(@NotNull String noteId);

    ResponseValue<NoteModel> likeNote(String noteId);
    ResponseValue<NoteModel> publicNote(String noteId);
}
