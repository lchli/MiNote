package com.lch.menote.common.base

import android.app.Activity
import android.support.v7.app.AppCompatActivity

/**
 * Created by lchli on 2016/8/10.
 */
abstract class BaseAppCompatActivity : AppCompatActivity() {

    protected fun activity(): Activity {
        return this
    }
}
