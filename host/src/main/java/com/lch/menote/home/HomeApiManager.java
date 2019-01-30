package com.lch.menote.home;

import android.content.Context;

/**
 * Created by Administrator on 2019/1/29.
 * called after module inited.
 */

public final class HomeApiManager implements HomeApi {
    private HomeApi impl;

    private static final HomeApiManager INS = new HomeApiManager();

    public static HomeApiManager getINS() {
        return INS;
    }

    public void initImpl(HomeApi impl) {
        this.impl = impl;
    }


    @Override
    public void launchHome(Context context) {
        impl.launchHome(context);
    }
}
