package com.lch.menote.note.data.db

import android.content.Context
import com.lch.menote.note.data.db.gen.DaoMaster
import com.lch.menote.note.data.db.gen.DaoSession
import com.lch.menote.note.data.db.gen.NoteDao

/**
 * Created by Administrator on 2017/9/21.
 */
private var noteDaoSession: DaoSession? = null

private fun daoSession(context: Context): DaoSession {
    if (noteDaoSession == null) {
        val helper = ReleaseDbOpenHelper(context, "note-db")
        noteDaoSession = DaoMaster(helper.writableDb).newSession()
    }
    return noteDaoSession!!
}

internal fun noteDao(context: Context): NoteDao {
    return daoSession(context).noteDao
}