package com.lch.menote.note.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.note.R
import kotlinx.android.synthetic.main.fragment_local_note_list.*

/**
 * Created by lchli on 2016/8/10.
 */
class LocalNoteFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_local_note_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty_widget.addEmptyText("no data")

        moduleListRecyclerView.setHasFixedSize(true)
        moduleListRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        moduleListRecyclerView.addItemDecoration(GridDividerDecoration(moduleListRecyclerView.context, GridDividerDecoration.VERTICAL_LIST))

        moduleListRecyclerView.setPinnedAdapter(null)


    }

}