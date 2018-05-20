package com.lch.menote.user.domain

import com.lch.menote.userapi.User

/**
 * Created by lichenghang on 2017/10/3.
 */
data class LoginResponse(val code: Int, val msg: String?,val user: User?=null)