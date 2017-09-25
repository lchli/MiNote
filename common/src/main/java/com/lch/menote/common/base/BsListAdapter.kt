package com.lch.menote.common.base

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter

abstract class BsListAdapter<T> : BaseAdapter() {

    private var mDatas: List<T>? = null
    private var isScrolling = false


    override fun getCount(): Int {
        return mDatas?.size ?: 0
    }

    override fun getItem(position: Int): T? {

        return mDatas?.get(position)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    fun refresh(datas: List<T>) {
        mDatas = datas
        this.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView == null || (convertView.tag as AbsViewHolder).viewType != getItemViewType(position)) {
            val vh = onCreateViewHolder(parent, getItemViewType(position))
            onBindViewHolder(vh, position)
            val item = vh.itemView
            item.tag = vh
            item
        } else {
            val vh = convertView.tag as AbsViewHolder
            onBindViewHolder(vh, position)
            convertView
        }
    }

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsViewHolder

    abstract fun onBindViewHolder(holder: AbsViewHolder, position: Int)

    abstract class AbsViewHolder(var itemView: View, var viewType: Int)


    val listOnScrollListener: AbsListView.OnScrollListener
        get() = object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    isScrolling = false
                    notifyDataSetChanged()
                } else {
                    isScrolling = true
                }
            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

            }
        }

}
