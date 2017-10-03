package com.lch.menote.note.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.common.route.UserMod
import com.lch.menote.common.toast
import com.lch.menote.common.util.EventBusUtils
import com.lch.menote.note.R
import com.lch.menote.note.data.DataSources
import com.lch.menote.note.domain.CloudNoteListChangedEvent
import com.lch.menote.note.domain.Note
import com.lch.route.noaop.Android
import com.lch.route.noaop.lib.RouteEngine
import kotlinx.android.synthetic.main.fragment_cloud_note_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by lchli on 2016/8/10.
 */
class CloudNoteFragment : BaseFragment() {

    private lateinit var mCloudNoteAdapter: CloudNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusUtils.register(this)

        mCloudNoteAdapter = CloudNoteAdapter(context)
    }

    override fun onDestroy() {
        EventBusUtils.unregister(this)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_cloud_note_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moduleListRecyclerView.setHasFixedSize(true)
        moduleListRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        moduleListRecyclerView.addItemDecoration(GridDividerDecoration(moduleListRecyclerView.context, GridDividerDecoration.VERTICAL_LIST))
        moduleListRecyclerView.adapter = mCloudNoteAdapter

        getNoteAsync()
    }


    private fun getNoteAsync() {

        val job = async(CommonPool) {
            try {
                val mod = RouteEngine.getModule(UserMod.MODULE_NAME) as? UserMod ?: throw Exception("can not find user module")

                val userId = mod.userId() ?: throw Exception("not login")


                DataSources.netNote.queryNotes(useId = userId)

            } catch (e: Exception) {
                e.printStackTrace()
                e
            }

        }

        launch(Android) {
            val e = job.await()

            if (e is Exception) {
                getContext().toast(e.message)
                mCloudNoteAdapter.refresh(null)

            } else {
                mCloudNoteAdapter.refresh(e as? List<Note>)
            }


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: CloudNoteListChangedEvent) {
        getNoteAsync()
    }
}
