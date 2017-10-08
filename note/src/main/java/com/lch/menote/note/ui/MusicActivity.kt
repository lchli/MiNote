package com.lch.menote.note.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.lch.menote.common.launchActivity
import com.lch.menote.common.log
import com.lch.menote.common.saveViewBmpToSdcard
import com.lch.menote.common.util.AppListItemAnimatorUtils
import com.lch.menote.common.util.EventBusUtils
import com.lch.menote.common.util.TimeUtils
import com.lch.menote.common.util.UUIDUtils
import com.lch.menote.note.R
import com.lch.menote.note.data.DataSources
import com.lch.menote.note.data.NoteRepo
import com.lch.menote.note.domain.LinkData
import com.lch.menote.note.domain.LocalNoteListChangedEvent
import com.lch.menote.note.domain.MusicData
import com.lch.menote.note.domain.Note
import com.lch.menote.note.helper.JsonHelper
import kotlinx.android.synthetic.main.activity_edit_music.*

class MusicActivity : AppCompatActivity() {

    private lateinit var mMusicAdapter: MusicAdapter
    private val datas = mutableListOf<MusicData>()
    private var controllViewTotalHeight = -1
    private var courseUUID: String? = null

    companion object {

        private const val EXTRA_NOTE = "EXTRA_NOTE"

        fun launch(context: Context, note: Note? = null) {
            val it = Intent(context, MusicActivity::class.java)
            if (note != null) {
                it.putExtra(EXTRA_NOTE, note)
            }
            context.launchActivity(it)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_music)

        musicGrid.setHasFixedSize(true)
        musicGrid.layoutManager = GridLayoutManager(this, 15)
        musicGrid.addItemDecoration(GridDividerDecoration(this, GridDividerDecoration.VERTICAL_LIST, SizeUtils.dp2px(5f)))

        mMusicAdapter = MusicAdapter(this)
        musicGrid.adapter = mMusicAdapter

        val note = intent.getSerializableExtra(EXTRA_NOTE) as? Note
        if (note != null) {
            val oldDatas: List<MusicData>? = Gson().fromJson(note.content, JsonHelper.getMusicListTypeToken())
            if (oldDatas != null) {
                datas.addAll(oldDatas)
            }
            courseUUID = note.uid
            tv_note_category.text = note.type
            et_note_title.setText(note.title)

            refresh()
        } else {
            courseUUID = UUIDUtils.uuid()
        }

    }


    fun addTune(v: View) {
        var input = etTune.text.toString()
        when {
            input.startsWith("..") -> {
            }
            input.startsWith(".") -> {
                input = " " + input

            }
            else -> {
                input = "  " + input
            }

        }

        var newText = ""

        for (i in input.indices) {
            newText = if (i != input.length - 1) {
                newText + input[i] + "\n"
            } else {
                newText + input[i]
            }
        }

        if (mMusicAdapter.currentSelected.isNotEmpty()) {
            val selected = mMusicAdapter.currentSelected[0]
            selected.tuneTexts = newText
        } else {
            datas.add(MusicData(mutableListOf(), newText, UUIDUtils.uuid()))
        }

        refresh()
    }


    fun addLine(v: View) {
        try {
            if (mMusicAdapter.currentSelected.size < 2) {
                return
            }

            mMusicAdapter.currentSelected[0].links.add(LinkData(mMusicAdapter.currentSelected[1], etTune.text.toString()))

            refresh()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteLine(v: View) {
        try {
            if (mMusicAdapter.currentSelected.size < 2) {
                return
            }

            mMusicAdapter.currentSelected[0].links.remove(LinkData(mMusicAdapter.currentSelected[1]))

            refresh()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun back(v: View) {
        if (datas.size > 0) {
            datas.removeAt(datas.size - 1)
        }

        refresh()
    }

    fun clear(v: View) {
        datas.clear()

        refresh()
    }

    fun save(v: View) {
        musicGrid.saveViewBmpToSdcard()

    }


    fun toolBar(v: View) {

        if (controllViewTotalHeight == -1) {
            controllViewTotalHeight = controllView.height
            log("controllViewTotalHeight=$controllViewTotalHeight")
        }

        if (controllView.height <= 0) {
            AppListItemAnimatorUtils.startHeightAnim(controllView, 0, controllViewTotalHeight)
        } else {
            AppListItemAnimatorUtils.startHeightAnim(controllView, controllViewTotalHeight, 0)
        }

    }

    fun saveDraft(v: View) {
        val content = Gson().toJson(datas)

        val note = Note()
        note.type = tv_note_category.text.toString()
        note.title = et_note_title.text.toString()
        note.uid = courseUUID
        note.lastModifyTime = TimeUtils.getTime(System.currentTimeMillis())
        note.content = content
        note.category = Note.CAT_MUSIC

        NoteRepo(DataSources.localNote).save(note)
        EventBusUtils.post(LocalNoteListChangedEvent())

        finish()
    }

    private fun refresh() {
        mMusicAdapter.refresh(datas)
        musicGrid.drawMusic(datas)
    }
}
