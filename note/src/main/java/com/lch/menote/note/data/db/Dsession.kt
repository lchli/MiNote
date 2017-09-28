package com.lch.menote.note.data.db

import android.content.Context
import com.lch.menote.common.route.UserMod
import com.lch.menote.note.data.db.gen.DaoMaster
import com.lch.menote.note.data.db.gen.DaoSession
import com.lch.menote.note.data.db.gen.NoteDao
import com.lch.menote.note.helper.NOTE_DB
import com.lch.route.noaop.lib.RouteEngine

/**
 * Created by Administrator on 2017/9/25.
 */
object Dsession {

    private var noteDaoSession: DaoSession? = null

    private fun daoSession(context: Context): DaoSession {
        if (noteDaoSession == null) {
            val helper = AppDbOpenHelper(context, NOTE_DB, isSdcardDatabase = true)
            val pwd = (RouteEngine.getModule(UserMod.MODULE_NAME) as? UserMod)?.getLockPwd()
            noteDaoSession = DaoMaster(helper.getEncryptedWritableDb(pwd!!)).newSession()
        }
        return noteDaoSession!!
    }

    internal fun destroy() {

        noteDaoSession?.database?.close()
        noteDaoSession?.clear()
        noteDaoSession = null
    }

    internal fun noteDao(context: Context): NoteDao {
        return daoSession(context).noteDao
    }
}