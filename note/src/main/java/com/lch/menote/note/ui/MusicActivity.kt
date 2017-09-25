package com.lch.menote.note.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lch.menote.note.R
import com.lch.menote.note.domain.LinkData
import com.lch.menote.note.domain.MusicData
import kotlinx.android.synthetic.main.activity_music.*

class MusicActivity : AppCompatActivity() {

    private lateinit var mMusicAdapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        mMusicAdapter = MusicAdapter(this)
        musicGrid.adapter = mMusicAdapter

        val datas = listOf(
                MusicData(listOf(LinkData(2, "3")), R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_add_note),
                MusicData(listOf(LinkData(8)), R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera),
                MusicData(null, R.drawable.ic_camera)
        )
        mMusicAdapter.refresh(datas)
        musicGrid.drawMusic(datas)
    }

}
