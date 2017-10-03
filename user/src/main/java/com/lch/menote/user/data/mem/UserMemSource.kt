package com.lch.menote.user.data.mem

import com.blankj.utilcode.util.EncryptUtils
import com.lch.menote.user.data.UserSource
import com.lch.menote.user.domain.User

/**
 * Created by lichenghang on 2017/10/3.
 */
object UserMemSource : UserSource {

    private var lockPwd = ""

    override fun getUser(name: String, pwd: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addUser(name: String, pwd: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveLockPwd(pwd: String) {
        lockPwd = EncryptUtils.encryptMD5ToString(pwd)
    }

    override fun getLockPwd(): String? {
        return lockPwd
    }

    override fun clearLockPwd() {
        lockPwd = ""
    }

    override fun getUser(): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCurrentUser() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUser(userId: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}