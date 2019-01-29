package com.lch.menote.user.domain;

import com.lch.menote.user.datainterface.AppUpdateInfoDataSource;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * 传给用例的一定是数据源的抽象接口。model转换器？如果从数据源拿到的是entity应该使用转换器转换为model
 */
public class CheckAppUpdateCase extends UseCase<CheckAppUpdateCase.Params, String> {

    public static class Params {

        public int currentVersionCode;
    }


    private final AppUpdateInfoDataSource appUpdateInfoDataSource;


    public CheckAppUpdateCase(AppUpdateInfoDataSource source) {
        this.appUpdateInfoDataSource = source;
    }

    @Override
    protected ResponseValue<String> execute(Params parameters) {
        return appUpdateInfoDataSource.getUpdateInfo(parameters.currentVersionCode);
    }
}
