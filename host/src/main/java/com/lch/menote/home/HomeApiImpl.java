package com.lch.menote.home;

import android.content.Context;

import com.lchli.utils.tool.Navigator;

/**
 * Created by Administrator on 2019/1/30.
 */

public class HomeApiImpl implements HomeApi {
    @Override
    public void launchHome(Context context) {
        Navigator.launchActivity(context,HomeActivity.class);
    }
}
