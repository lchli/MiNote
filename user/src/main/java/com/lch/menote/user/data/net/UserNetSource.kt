package com.lch.menote.user.data.net

import com.lch.menote.common.Glo
import com.lch.menote.user.data.UserSource
import com.lch.menote.user.domain.User

/**
 * Created by lichenghang on 2017/10/3.
 */
object UserNetSource : UserSource {

    override fun addUser(name: String, pwd: String): User? {
        val api = Glo.noteRetrofit.create(UserApi::class.java)
        val resp = api.register(name, pwd).execute()
        val body = resp.body()
        if (body != null) {
            if (body.code == 0) {
                return body.user
            } else {
                throw Exception(body.msg)
            }
        }

        throw Exception(resp.message())
    }

    override fun getUser(name: String, pwd: String): User? {

        val api = Glo.noteRetrofit.create(UserApi::class.java)
        val resp = api.login(name, pwd).execute()
        val body = resp.body()
        if (body != null) {
            if (body.code == 0) {
                return body.user
            } else {
                throw Exception(body.msg)
            }
        }

        throw Exception(resp.message())
    }

    override fun getUser(userId: String): User? {
        val api = Glo.noteRetrofit.create(UserApi::class.java)
        val resp = api.queryUser(userId).execute()
        val body = resp.body()
        if (body != null) {
            if (body.code == 0) {
                return body.user
            } else {
                throw Exception(body.msg)
            }
        }

        throw Exception(resp.message())

    }

    override fun addUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun getUser(): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCurrentUser() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}