package com.lch.menote.homeapi;

import java.util.Map;

/**
 * Created by lichenghang on 2018/5/19.
 */

public interface HomeRouteApi {

   String MODULE_NAME = "home";

    void launchHome(Map<String, String> params);
}
