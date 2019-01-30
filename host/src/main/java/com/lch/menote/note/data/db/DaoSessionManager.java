package com.lch.menote.note.data.db;

import android.content.Context;

import com.lch.menote.ConstantUtil;
import com.lch.menote.note.data.db.gen.DaoMaster;
import com.lch.menote.note.data.db.gen.DaoSession;
import com.lch.menote.note.data.db.gen.NoteDao;
import com.lch.menote.user.UserApiManager;

public final class DaoSessionManager {

    private static DaoSession noteDaoSession;

    private static DaoSession daoSession(Context context) {
        if (noteDaoSession == null) {
            AppDbOpenHelper helper = new AppDbOpenHelper(context, ConstantUtil.NOTE_DB, true);

            String pwd = UserApiManager.getINS().getLockPwd();

            noteDaoSession = new DaoMaster(helper.getEncryptedWritableDb(pwd)).newSession();
        }
        return noteDaoSession;
    }

    public static void destroy() {
        try {
            noteDaoSession.getDatabase().close();
            noteDaoSession.clear();
            noteDaoSession = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static NoteDao noteDao(Context context) {
        return daoSession(context).getNoteDao();
    }
}
