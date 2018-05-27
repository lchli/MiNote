package com.lch.menote.user.data

import com.lch.menote.userapi.User


/**
 * Created by lichenghang on 2017/10/3.
 */
interface UserSource {

    fun getUser(userName: String, userPwd: String): User?
    fun getUser(): User?
    fun getUser(userId:String): User?
    fun addUser(userName: String, userPwd: String): User?
    fun addUser(user: User)
    fun removeCurrentUser()

    fun saveLockPwd(pwd: String)
    fun getLockPwd(): String?
    fun clearLockPwd()
}