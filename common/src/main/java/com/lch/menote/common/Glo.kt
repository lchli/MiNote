package com.lch.menote.common

import android.content.Context
import com.lch.menote.common.netkit.file.FileManager
import com.lch.menote.common.netkit.string.Retrofits
import retrofit2.Retrofit
import kotlin.properties.Delegates

/**
 * Created by Administrator on 2017/9/30.
 */
object Glo {
    var context: Context by Delegates.notNull()
    var fileManager: FileManager by Delegates.notNull()
    private const val NOTE_BASE_URL = "http://192.168.1.101:8080"
    val noteRetrofit: Retrofit = Retrofits.get(NOTE_BASE_URL)

    fun init(ctx: Context) {
        context = ctx
        fileManager = FileManager()
    }


}