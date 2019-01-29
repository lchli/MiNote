package com.lch.menote.note.ui

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.lch.menote.note.model.MusicData
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.BaseRecyclerAdapter

/**
 * Created by Administrator on 2017/9/25.
 */
class MusicAdapter(private val context: Context) : BaseRecyclerAdapter<MusicData>() {

     var currentSelected = mutableListOf<MusicData>()


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as? MusicHolder ?: return
        val data = getItem(position) ?: return

        with(h.itemView as TuneView) {
            text = data.tuneTexts

            if (currentSelected.contains(data)) {
                setBackgroundColor(Color.BLUE)
            } else {
                setBackgroundColor(Color.TRANSPARENT)
            }

            setOnClickListener {
                if (currentSelected.contains(data)) {
                    currentSelected.remove(data)
                } else {
                    currentSelected.add(data)
                }

                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MusicHolder(TuneView(context), viewType)
    }


    internal inner class MusicHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView)
}