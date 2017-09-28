package com.lch.menote.note.route

import android.content.Context
import android.support.v4.app.Fragment
import cn.finalteam.galleryfinal.CoreConfig
import cn.finalteam.galleryfinal.FunctionConfig
import cn.finalteam.galleryfinal.GalleryFinal
import cn.finalteam.galleryfinal.ThemeConfig
import com.lch.menote.common.route.NoteMod
import com.lch.menote.note.data.NoteRepo
import com.lch.menote.note.data.db.Dsession
import com.lch.menote.note.helper.DB_DIR
import com.lch.menote.note.helper.GlideImageLoader
import com.lch.menote.note.ui.CloudNoteFragment
import com.lch.menote.note.ui.LocalNoteFragment
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService
import com.lch.route.noaop.lib.Router
import org.apache.commons.io.FileUtils
import java.io.File

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService(NoteMod.MODULE_NAME)
class NoteModule : Router, NoteMod {

    override fun init(context: Context) {
        NoteRepo.init(context)

        val functionConfig = FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build()

        val b = CoreConfig.Builder(context, GlideImageLoader(), ThemeConfig.DEFAULT)
                .setFunctionConfig(functionConfig)
                .build()
        GalleryFinal.init(b)
    }

    @RouteMethod(NoteMod.LOCAL_NOTE)
    override fun localFrament(params: Map<String, String>?): Fragment {
        return LocalNoteFragment()
    }

    @RouteMethod(NoteMod.CLOUD_NOTE)
    override fun cloudFragment(params: Map<String, String>?): Fragment {
        return CloudNoteFragment()
    }

    override fun clearDB(params: Map<String, String>?) {
        try {
            FileUtils.deleteDirectory(File(DB_DIR))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAppBackground(params: Map<String, String>?) {
        Dsession.destroy()
    }
}