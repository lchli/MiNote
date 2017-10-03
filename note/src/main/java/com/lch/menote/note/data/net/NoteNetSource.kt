package com.lch.menote.note.data.net

import com.lch.menote.common.Glo
import com.lch.menote.common.logIfDebug
import com.lch.menote.note.data.NoteSource
import com.lch.menote.note.domain.Note
import com.lch.route.noaop.lib.RouteEngine

/**
 * Created by lichenghang on 2017/10/3.
 */
class NoteNetSource : NoteSource {

    override fun queryNotes(tag: String?, title: String?, sortTimeAsc: Boolean, useId: String): List<Note>? {

        val api = Glo.noteRetrofit.create(NoteService::class.java)
        val resp = api.queryAllNote(useId, "").execute()
        val body = resp.body()

        RouteEngine.context.logIfDebug(body.toString())

        if (body != null) {
            if (body.code == 0) {
                return body.notes
            } else {
                throw Exception(body.msg)
            }
        }

        throw Exception(resp.message())
    }

    override fun save(note: Note) {
    }

    override fun delete(note: Note) {
    }
}