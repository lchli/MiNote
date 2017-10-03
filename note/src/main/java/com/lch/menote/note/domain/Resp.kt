package com.lch.menote.note.domain

/**
 * Created by lichenghang on 2017/10/3.
 */
data class AllNoteResponse(val code: Int, val msg: String?,val notes: List<Note>?)

data class BaseResponse(val code: Int, val msg: String?)