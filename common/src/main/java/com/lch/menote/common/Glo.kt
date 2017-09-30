package com.lch.menote.common

import android.content.Context
import com.lch.menote.common.netkit.file.FileManager
import kotlin.properties.Delegates

/**
 * Created by Administrator on 2017/9/30.
 */
object Glo {
    var context: Context by Delegates.notNull()
    var fileManager: FileManager by Delegates.notNull()

    fun init(ctx: Context) {
        context = ctx
        fileManager = FileManager(context)
    }
}