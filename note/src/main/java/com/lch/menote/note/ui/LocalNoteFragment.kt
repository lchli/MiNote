package com.lch.menote.note.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.common.showListDialog
import com.lch.menote.common.util.EventBusUtils
import com.lch.menote.note.R
import com.lch.menote.note.data.DataSources
import com.lch.menote.note.data.NoteRepo
import com.lch.menote.note.domain.LocalNoteListChangedEvent
import com.lch.route.noaop.Android
import com.orhanobut.dialogplus.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_local_note_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by lchli on 2016/8/10.
 */
class LocalNoteFragment : BaseFragment() {

    private var notesAdp: LocalNoteListAdapter = LocalNoteListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusUtils.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_local_note_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty_widget.addEmptyText("no data")

        moduleListRecyclerView.setHasFixedSize(true)
        moduleListRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        moduleListRecyclerView.addItemDecoration(GridDividerDecoration(moduleListRecyclerView.context, GridDividerDecoration.VERTICAL_LIST))
        moduleListRecyclerView.setPinnedAdapter(notesAdp)

        fab.setOnClickListener {
            context.showListDialog(OnItemClickListener { dialog, item, view, position ->
                when (position) {
                    0 -> {
                        dialog.dismiss()
                        EditNoteActivity.startSelf(context)
                    }
                    1 -> {
                        dialog.dismiss()
                        MusicActivity.launch(context)
                    }
                }
            }, items = listOf("创建笔记", "创建音乐"))

        }

        queryNotesAsync()

    }


    private fun queryNotesAsync() {
        val job = async(CommonPool) {
            NoteRepo(DataSources.localNote).queryNotesWithCat()
        }

        launch(Android) {
            val result = job.await()
            notesAdp.refresh(result)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: LocalNoteListChangedEvent) {
        queryNotesAsync()
    }
}