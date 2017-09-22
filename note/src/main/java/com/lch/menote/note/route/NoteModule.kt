package com.lch.menote.note.route

import android.content.Context
import android.support.v4.app.Fragment
import com.lch.menote.note.data.NoteRepo
import com.lch.menote.note.ui.CloudNoteFragment
import com.lch.menote.note.ui.LocalNoteFragment
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService
import com.lch.route.noaop.lib.Router

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService("note")
class NoteModule : Router {

    override fun init(context: Context) {
        NoteRepo.init(context)
    }

    @RouteMethod("local")
    fun localFrament(params: Map<String, String>): Fragment {
        return LocalNoteFragment()
    }

    @RouteMethod("cloud")
    fun cloudFragment(params: Map<String, String>): Fragment {
        return CloudNoteFragment()
    }

}