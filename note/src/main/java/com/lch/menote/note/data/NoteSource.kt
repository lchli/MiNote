package com.lch.menote.note.data

import com.lch.menote.note.domain.Note
import com.lch.menote.note.domain.NoteModel
import com.lch.menote.note.domain.QueryNoteResponse
import com.lch.netkit.common.mvc.ResponseValue

/**
 * Created by Administrator on 2017/9/22.
 */
interface NoteSource {

    fun queryNotes(tag: String?=null, title: String?=null, sortTimeAsc: Boolean=true,useId:String=""): ResponseValue<QueryNoteResponse>

    fun save(note: NoteModel): ResponseValue<Void>

    fun delete(note: Note)

}