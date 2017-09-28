package com.lch.menote

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lch.menote.common.route.UserMod
import com.lch.route.noaop.lib.RouteEngine

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (RouteEngine.getModule(UserMod.MODULE_NAME) as? UserMod)?.lockPwdPage()

        finish()
    }
}
