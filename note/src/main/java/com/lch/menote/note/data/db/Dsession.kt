package com.lch.menote.note.data.db

import android.content.Context
import com.lch.menote.note.data.db.gen.DaoMaster
import com.lch.menote.note.data.db.gen.DaoSession
import com.lch.menote.note.data.db.gen.NoteDao
import com.lch.menote.note.helper.ConstantUtil
import com.lch.menote.userapi.UserRouteApi
import com.lch.route.noaop.lib.RouteEngine

/**
 * Created by Administrator on 2017/9/25.
 */
object Dsession {

    private var noteDaoSession: DaoSession? = null

    private fun daoSession(context: Context): DaoSession {
        if (noteDaoSession == null) {
            val helper = AppDbOpenHelper(context, ConstantUtil.NOTE_DB, isSdcardDatabase = false)
            val pwd = (RouteEngine.getModule(UserRouteApi.MODULE_NAME) as? UserRouteApi)?.getLockPwd(null)
            noteDaoSession = DaoMaster(helper.getEncryptedWritableDb(pwd!!)).newSession()
        }
        return noteDaoSession!!
    }

     fun destroy() {

        noteDaoSession?.database?.close()
        noteDaoSession?.clear()
        noteDaoSession = null
    }

    internal fun noteDao(context: Context): NoteDao {
        return daoSession(context).noteDao
    }
}