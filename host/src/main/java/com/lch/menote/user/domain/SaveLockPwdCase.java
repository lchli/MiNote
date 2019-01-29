package com.lch.menote.user.domain;

import android.text.TextUtils;

import com.lch.menote.user.datainterface.PwdSource;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * 传给用例的一定是数据源的抽象接口。model转换器？如果从数据源拿到的是entity应该使用转换器转换为model
 */
public class SaveLockPwdCase extends UseCase<SaveLockPwdCase.Params, Void> {


    private final PwdSource dataSource;


    public static class Params {

        public String pwd;
    }


    public SaveLockPwdCase(PwdSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected ResponseValue<Void> execute(Params parameters) {
        if (TextUtils.isEmpty(parameters.pwd)) {
            return new ResponseValue<Void>().setErrorMsg("密码为空.");
        }
        return dataSource.saveLockPwd(parameters.pwd);
    }
}
