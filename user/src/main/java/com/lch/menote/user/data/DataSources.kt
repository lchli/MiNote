package com.lch.menote.user.data

import com.lch.menote.user.data.mem.UserMemSource
import com.lch.menote.user.data.net.UserNetSource
import com.lch.menote.user.data.sp.UserSpSource

/**
 * Created by lichenghang on 2017/10/3.
 */
object DataSources {

    val net = UserNetSource
    val mem = UserMemSource
    val sp = UserSpSource
}