package com.lch.menote.note.data.db;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.note.data.db.gen.NoteDao;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.note.domain.QueryNoteResponse;
import com.lch.menote.note.helper.ModelMapper;
import com.lch.netkit.common.mvc.MvcError;
import com.lch.netkit.common.mvc.ResponseValue;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.lch.menote.note.data.db.DatabseNoteRepo.LocalNoteQuery.SORT_ASC;

public class DatabseNoteRepo  {

    public static class LocalNoteQuery {
        public static final String SORT_ASC="asc";
        public static final String SORT_DESC="desc";
        private String sortDiretion=SORT_ASC;
        private String tag;
        private String title;
        private String useId;

        public static LocalNoteQuery newInstance() {
            return new LocalNoteQuery();
        }

        public LocalNoteQuery setSortDiretion(String sortDiretion) {
            this.sortDiretion = sortDiretion;
            return this;
        }


        public LocalNoteQuery setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public LocalNoteQuery setTitle(String title) {
            this.title = title;
            return this;
        }

        public LocalNoteQuery setUseId(String useId) {
            this.useId = useId;
            return this;
        }
    }

    private Context context;

    public DatabseNoteRepo(Context context) {
        this.context = context.getApplicationContext();
    }

    public ResponseValue<QueryNoteResponse> queryNotes(LocalNoteQuery query) {
        ResponseValue<QueryNoteResponse> res = new ResponseValue<>();

        try {

            QueryBuilder<Note> builder = DaoSessionManager.noteDao(context).queryBuilder();
            if (!TextUtils.isEmpty(query.title)) {
                builder.where(NoteDao.Properties.Title.like("%$title%"));
            }

            if (!TextUtils.isEmpty(query.tag)) {
                builder.where(NoteDao.Properties.Type.eq(query.tag));
            }

            if (SORT_ASC.equals(query.sortDiretion)) {
                builder.orderAsc(NoteDao.Properties.Type).orderAsc(NoteDao.Properties.LastModifyTime);
            } else {
                builder.orderAsc(NoteDao.Properties.Type).orderDesc(NoteDao.Properties.LastModifyTime);
            }

            QueryNoteResponse r = new QueryNoteResponse();
            ArrayList<NoteModel> models = new ArrayList<>();

            List<Note> notes = builder.list();
            if (notes != null) {
                for (Note n : notes) {
                    models.add(ModelMapper.from(n));
                }
            }

            r.data = models;

            res.data = r;

        } catch (Exception e) {
            e.printStackTrace();
            detectLockError(e);
            res.err = new MvcError(e.getMessage());
        }

        return res;
    }

    public ResponseValue<Void> save(NoteModel note) {
        ResponseValue<Void> ret = new ResponseValue<>();

        try {

            DaoSessionManager.noteDao(context).insertOrReplace(ModelMapper.from(note));
        } catch (Exception e) {
            e.printStackTrace();
            detectLockError(e);
            ret.setErrMsg(e.getMessage());
        }

        return ret;
    }

    public ResponseValue<Void> delete(Note note) {
        ResponseValue<Void> ret = new ResponseValue<>();

        try {
            DaoSessionManager.noteDao(context).delete(note);
        } catch (Exception e) {
            e.printStackTrace();
            detectLockError(e);
            ret.setErrMsg(e.getMessage());
        }

        return ret;
    }

    private void detectLockError(Exception e) {
        if (e.getMessage().contains("encrypted")) {
            ToastUtils.showShort("数据解锁失败！如果忘记密码只能重置");
        }
    }
}
