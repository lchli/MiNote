package com.lch.menote.note.data.db;

import android.content.Context;

import com.lch.menote.note.data.db.gen.DaoMaster;
import com.lch.menote.note.data.db.gen.DaoSession;
import com.lch.menote.note.data.db.gen.NoteDao;
import com.lch.menote.note.helper.ConstantUtil;
import com.lch.menote.note.route.RouteCall;
import com.lch.menote.user.route.UserRouteApi;

public final class DaoSessionManager {

    private static DaoSession noteDaoSession;

    private static DaoSession daoSession(Context context) {
        if (noteDaoSession == null) {
            AppDbOpenHelper helper = new AppDbOpenHelper(context, ConstantUtil.NOTE_DB, true);
            UserRouteApi userMod = RouteCall.getUserModule();
            String pwd = null;

            if (userMod != null) {
                pwd = userMod.getLockPwd(null);
            }

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
