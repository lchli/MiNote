package com.lch.menote.note.data

import android.content.Context
import com.lch.menote.note.data.db.NoteTable
import com.lch.menote.note.domain.HeadData
import com.lch.menote.note.domain.Note
import com.lch.menote.note.domain.NotePinedData
import com.lch.menote.note.helper.VIEW_TYPE_PINED
import kotlin.properties.Delegates


/**
 * Created by Administrator on 2017/9/22.
 */
internal object NoteRepo {
    private var noteSource: NoteSource by Delegates.notNull()


    fun init(context: Context) {
        noteSource = NoteTable(context)
    }

    fun save(note: Note) {
        noteSource.save(note)
    }

    fun queryNotes(): List<Note>? {

        return noteSource.queryNotes()
    }

    fun queryNotesWithCat(): List<Any>? {
        val all = ArrayList<Any>()
        all.add(HeadData())
        val notes = noteSource.queryNotes() ?: return all

        var preType = ""

        for (note in notes) {
            val currentType = note.type
            if (preType != currentType) {
                all.add(NotePinedData(VIEW_TYPE_PINED, currentType))
                preType = currentType
            }

            all.add(note)
        }

        return all
    }
}