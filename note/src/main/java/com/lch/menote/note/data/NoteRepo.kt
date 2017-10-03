package com.lch.menote.note.data

import com.lch.menote.note.domain.HeadData
import com.lch.menote.note.domain.Note
import com.lch.menote.note.domain.NotePinedData
import com.lch.menote.note.helper.VIEW_TYPE_PINED


/**repo can handle other logic,eg cache.
 * Created by Administrator on 2017/9/22.
 */
internal class NoteRepo(val noteSource: NoteSource) {


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

    fun delete(note: Note) {
        noteSource.delete(note)
    }

}