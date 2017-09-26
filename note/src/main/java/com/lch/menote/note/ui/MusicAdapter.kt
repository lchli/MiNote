package com.lch.menote.note.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.base.BsListAdapter
import com.lch.menote.note.R
import com.lch.menote.note.domain.MusicData
import kotlinx.android.synthetic.main.note_list_item_music.view.*

/**
 * Created by Administrator on 2017/9/25.
 */
class MusicAdapter(private val context: Context) : BsListAdapter<MusicData>() {


    override fun onBindViewHolder(holder: AbsViewHolder, position: Int) {
        val h = holder as? MusicHolder ?: return
        val data = getItem(position) ?: return
        (h.itemView as TuneView).setResource(data.tuneTexts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsViewHolder {
        return MusicHolder(TuneView(context), viewType)
    }


    internal inner class MusicHolder(itemView: View, viewType: Int) : AbsViewHolder(itemView, viewType)
}