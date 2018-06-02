package com.lch.menote.user.data.mem;

import com.blankj.utilcode.util.EncryptUtils;
import com.lch.netkit.common.mvc.ResponseValue;

public class MemUserRepo {

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
