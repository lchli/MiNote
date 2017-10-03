package com.lch.menote.note.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.lch.menote.note.R
import com.lch.menote.note.domain.Note
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.cloud_note_list_item.view.*

/**
 * Created by Administrator on 2017/9/25.
 */
internal class CloudNoteAdapter(private val context: Context) : BaseRecyclerAdapter<Note>() {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as? VH ?: return
        val data = getItem(position) ?: return

        with(h.itemView) {
            couse_title_textView.text = data.title
            course_time_textView.text = data.lastModifyTime

            setOnClickListener {
                CloudNoteDetailActivity.startSelf(context, data)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(View.inflate(context, R.layout.cloud_note_list_item, null), 0)
    }


    internal inner class VH(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView)
}