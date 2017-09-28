package com.lch.menote.user.data

import com.blankj.utilcode.util.EncryptUtils

/**
 * Created by Administrator on 2017/9/28.
 */
internal object UserRepo {

    private var lockPwd = ""

    fun saveLockPwd(pwd: String) {
        lockPwd = EncryptUtils.encryptMD5ToString(pwd)
    }

    fun getLockPwd(): String? {
        return lockPwd
    }

    fun clearLockPwd() {
        lockPwd = ""
    }
}