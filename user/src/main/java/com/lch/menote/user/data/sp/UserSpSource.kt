package com.lch.menote.user.data.sp

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.lch.menote.user.data.UserSource
import com.lch.menote.user.route.UserRouteApiImpl
import com.lch.menote.userapi.User

/**
 * Created by lichenghang on 2017/10/3.
 */
object UserSpSource : UserSource {

    private const val KEY_USER_SESSION = "KEY_USER_SESSION"

    override fun getUser(name: String, pwd: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUser(): User? {
        val json = SPUtils.getInstance(UserRouteApiImpl.SP).getString(KEY_USER_SESSION)
        return Gson().fromJson(json, User::class.java)
    }

    override fun addUser(name: String, pwd: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addUser(user: User) {
        SPUtils.getInstance(UserRouteApiImpl.SP).put(KEY_USER_SESSION, Gson().toJson(user))

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
        SPUtils.getInstance(UserRouteApiImpl.SP).remove(KEY_USER_SESSION)
    }

    override fun getUser(userId: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}