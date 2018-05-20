package com.lch.menote.common

import android.content.Context
import retrofit2.Retrofit
import kotlin.properties.Delegates

/**
 * Created by Administrator on 2017/9/30.
 */
object Glo {
    var context: Context by Delegates.notNull()
    private const val NOTE_BASE_URL = "http://192.168.1.101:8080"
    val noteRetrofit: Retrofit by Delegates.notNull()

    fun init(ctx: Context) {
        context = ctx
    }


}