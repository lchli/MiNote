package com.lch.menote.user.datainterface;

import com.lch.netkit.common.mvc.ResponseValue;

/**
 * Created by Administrator on 2018/12/27.
 */

public interface AppUpdateInfoDataSource {
    ResponseValue<String> getUpdateInfo(int currentVersionCode);
}
