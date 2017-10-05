package com.lch.menote.user.data

import com.lch.menote.common.route.model.User


/**
 * Created by lichenghang on 2017/10/3.
 */
interface UserSource {

    fun getUser(name: String, pwd: String): User?
    fun getUser(): User?
    fun getUser(userId:String): User?
    fun addUser(name: String, pwd: String): User?
    fun addUser(user: User)
    fun removeCurrentUser()

    fun saveLockPwd(pwd: String)
    fun getLockPwd(): String?
    fun clearLockPwd()
}