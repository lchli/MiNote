package com.lch.menote.common.base

import android.support.v4.app.Fragment


/**
 * Created by lchli on 2016/8/10.
 */
abstract class BaseFragment : Fragment() {

    var isInitLoadDataCalled = false

    open fun initLoadData() {
        //def impl.
    }
}
