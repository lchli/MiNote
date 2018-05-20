package com.lch.menote.note.data

import android.content.Context
import com.lch.menote.note.data.db.DbNoteRepo
import com.lch.menote.note.data.net.NetNoteRepo
import kotlin.properties.Delegates

/**
 * Created by lichenghang on 2017/10/3.
 */
internal object DataSources {

    var localNote: NoteSource by Delegates.notNull()
    var netNote: NoteSource by Delegates.notNull()

    fun init(context: Context) {
        localNote = DbNoteRepo(context)
        netNote = NetNoteRepo()
    }
}