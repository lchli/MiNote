package com.lch.menote.note.data;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.note.data.db.DaoSessionManager;
import com.lch.menote.note.data.db.gen.NoteDao;
import com.lch.menote.note.data.mapper.ModelMapper;
import com.lch.menote.note.datainterface.LocalNoteSource;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.data.entity.Note;
import com.lchli.arch.clean.ResponseValue;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;


public class DatabseNoteRepo implements LocalNoteSource {

    private Context context;

    public DatabseNoteRepo(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public ResponseValue<List<NoteModel>> queryNotes(LocalNoteQuery query) {
        ResponseValue<List<NoteModel>> res = new ResponseValue<>();

        try {

            QueryBuilder<Note> builder = DaoSessionManager.noteDao(context).queryBuilder();
            if (!TextUtils.isEmpty(query.title)) {
                builder.where(NoteDao.Properties.Title.like("%$title%"));
            }

            if (!TextUtils.isEmpty(query.tag)) {
                builder.where(NoteDao.Properties.Type.eq(query.tag));
            }

            if (LocalNoteQuery.SORT_ASC.equals(query.sortDiretion)) {
                builder.orderAsc(NoteDao.Properties.Type).orderAsc(NoteDao.Properties.LastModifyTime);
            } else {
                builder.orderAsc(NoteDao.Properties.Type).orderDesc(NoteDao.Properties.LastModifyTime);
            }

            ArrayList<NoteModel> models = new ArrayList<>();

            List<Note> notes = builder.list();
            if (notes != null) {
                for (Note n : notes) {
                    models.add(ModelMapper.from(n));
                }
            }

            res.data = models;

        } catch (Exception e) {
            e.printStackTrace();
            detectLockError(e);
            res.setErrorMsg(e.getMessage());
        }

        return res;
    }

    @Override
    public ResponseValue<Void> save(NoteModel note) {
        ResponseValue<Void> ret = new ResponseValue<>();

        try {
            DaoSessionManager.noteDao(context).insertOrReplace(ModelMapper.from(note));
        } catch (Exception e) {
            e.printStackTrace();
            detectLockError(e);
            ret.setErrorMsg(e.getMessage());
        }

        return ret;
    }

    @Override
    public ResponseValue<Void> delete(String noteId) {
        ResponseValue<Void> ret = new ResponseValue<>();

        try {
            DaoSessionManager.noteDao(context).deleteByKey(noteId);
        } catch (Exception e) {
            e.printStackTrace();
            detectLockError(e);
            ret.setErrorMsg(e.getMessage());
        }

        return ret;
    }

    private void detectLockError(Exception e) {
        if (e.getMessage().contains("encrypted")) {
            ToastUtils.showShort("数据解锁失败！如果忘记密码只能重置");
        }
    }
}
