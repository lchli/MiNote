package com.lch.menote.user.dataimpl;

import com.blankj.utilcode.util.EncryptUtils;
import com.lch.menote.user.datainterface.PwdSource;
import com.lchli.arch.clean.ResponseValue;


public class MemPwdSource implements PwdSource {

    private static String lockPwd = "";

    public ResponseValue<Void> saveLockPwd(String pwd) {
        lockPwd = EncryptUtils.encryptMD5ToString(pwd);
        return new ResponseValue<>();
    }

    public ResponseValue<String> getLockPwd() {
        ResponseValue<String> res = new ResponseValue<>();
        res.data = lockPwd;
        return res;
    }

    public ResponseValue<Void> clearLockPwd() {
        lockPwd = "";
        return new ResponseValue<>();
    }

}
