package com.lch.menote.home.route;

import android.content.Context;

import com.lch.menote.home.HomeActivity;
import com.lch.netkit.common.tool.Navigator;
import com.lch.route.noaop.lib.RouteService;
import com.lch.route.noaop.lib.Router;

import java.util.Map;

/**
 * Created by lichenghang on 2018/5/19.
 */
@RouteService(HomeRouteApi.MODULE_NAME)
public class HomeRouteApiImpl implements HomeRouteApi,Router {
    private Context mContext;

    @Override
    public void init(Context context) {
        mContext=context;
    }

    @Override
    public void launchHome(Map<String, String> params) {
        Navigator.launchActivity(mContext,HomeActivity.class);
    }
}
