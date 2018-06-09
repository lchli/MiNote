package com.lch.menote.app;

import com.lch.menote.app.model.ApkResponse;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.TaskExecutor;
import com.lch.netkit.common.tool.UiHandler;

/**
 * Created by lichenghang on 2018/6/9.
 */

public class ApkController {

    private ApkRepo apkRepo = new ApkRepo();

    public void checkUpdate(final int currentVersionCode, final ControllerCallback<ApkResponse> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<ApkResponse> res = apkRepo.getUpdate(currentVersionCode);

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(res);
                        }
                    }
                });
            }
        });

    }
}
