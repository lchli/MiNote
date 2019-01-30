package com.lch.menote.app;

import com.lch.menote.app.model.ApkResponse;
import com.lch.menote.utils.MvpViewUtils;
import com.lch.netkit.v2.common.NetworkResponse;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.utils.tool.TaskExecutor;


/**
 * Created by lichenghang on 2018/6/9.
 */

public class ApkController {

    private ApkRepo apkRepo = new ApkRepo();

    public void checkUpdate(final int currentVersionCode, final ControllerCallback<ApkResponse> callback) {
        final ControllerCallback<ApkResponse> cb= MvpViewUtils.newUiThreadProxy(callback);

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final NetworkResponse<ApkResponse> res = apkRepo.getUpdate(currentVersionCode);
                if(res.hasError()){
                    cb.onError(0,res.getErrorMsg());
                }else{
                    cb.onSuccess(res.data);
                }


            }
        });

    }
}
