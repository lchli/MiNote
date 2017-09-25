package com.lch.menote.note.data.db

import android.content.Context
import android.text.TextUtils
import com.lch.menote.note.data.NoteSource
import com.lch.menote.note.data.db.gen.NoteDao
import com.lch.menote.note.domain.Note

/**
 * Created by Administrator on 2017/9/22.
 */
class NoteTable(private val context: Context) : NoteSource {

    override fun queryNotes(tag: String?, title: String?, sortTimeAsc: Boolean): List<Note>? {
        try {

            val builder = Dsession.noteDao(context).queryBuilder()
            if (!TextUtils.isEmpty(title)) {
                builder.where(NoteDao.Properties.Title.like("%$title%"))
            }

            if (!TextUtils.isEmpty(tag)) {
                builder.where(NoteDao.Properties.Type.eq(tag))
            }

            if (sortTimeAsc) {
                builder.orderAsc(NoteDao.Properties.Type).orderAsc(NoteDao.Properties.LastModifyTime);
            } else {
                builder.orderAsc(NoteDao.Properties.Type).orderDesc(NoteDao.Properties.LastModifyTime);
            }

            return builder.list()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun save(note: Note) {
        try {
            Dsession.noteDao(context).insertOrReplace(note)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun delete(note: Note) {
        try {
            Dsession.noteDao(context).delete(note)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}