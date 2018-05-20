package com.lch.menote.note.data

import com.lch.menote.note.domain.Note
import com.lch.netkit.common.mvc.ResponseValue

/**
 * Created by Administrator on 2017/9/22.
 */
interface NoteSource {

    fun queryNotes(tag: String?=null, title: String?=null, sortTimeAsc: Boolean=true,useId:String=""): ResponseValue<List<Note>>

    fun save(note: Note)

    fun delete(note: Note)

}