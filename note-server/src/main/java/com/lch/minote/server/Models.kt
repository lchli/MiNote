package com.lch.minote.server

/**
 * Created by lichenghang on 2017/10/1.
 */

data class BaseResponse(val code: Int, val msg: String?)
data class LoginResponse(val code: Int, val msg: String?,val user: User?=null)
data class AllNoteResponse(val code: Int, val msg: String?,val notes: List<Note>?)