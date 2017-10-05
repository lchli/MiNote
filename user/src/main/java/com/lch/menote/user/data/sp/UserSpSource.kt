package com.lch.menote.user.data.sp

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.lch.menote.common.route.model.User
import com.lch.menote.user.data.UserSource
import com.lch.menote.user.route.UserModule

/**
 * Created by lichenghang on 2017/10/3.
 */
object UserSpSource : UserSource {

    private const val KEY_USER_SESSION = "KEY_USER_SESSION"

    override fun getUser(name: String, pwd: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUser(): User? {
        val json = SPUtils.getInstance(UserModule.SP).getString(KEY_USER_SESSION)
        return Gson().fromJson(json, User::class.java)
    }

    override fun addUser(name: String, pwd: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addUser(user: User) {
        SPUtils.getInstance(UserModule.SP).put(KEY_USER_SESSION, Gson().toJson(user))

    }

    override fun saveLockPwd(pwd: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLockPwd(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearLockPwd() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCurrentUser() {
        SPUtils.getInstance(UserModule.SP).remove(KEY_USER_SESSION)
    }

    override fun getUser(userId: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}