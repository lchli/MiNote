package com.lch.menote

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RouteCall.getUserModule()?.lockPwdPage(null)

        finish()
    }
}
