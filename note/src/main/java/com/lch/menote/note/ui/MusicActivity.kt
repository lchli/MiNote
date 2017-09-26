package com.lch.menote.note.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.lch.menote.common.drawBitmap
import com.lch.menote.common.util.BitmapScaleUtil
import com.lch.menote.common.util.ToastUtils
import com.lch.menote.common.util.UUIDUtils
import com.lch.menote.note.R
import com.lch.menote.note.domain.LinkData
import com.lch.menote.note.domain.MusicData
import com.lch.route.noaop.Android
import kotlinx.android.synthetic.main.activity_music.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.File

class MusicActivity : AppCompatActivity() {

    private lateinit var mMusicAdapter: MusicAdapter
    private val datas = mutableListOf<MusicData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        mMusicAdapter = MusicAdapter(this)
        musicGrid.adapter = mMusicAdapter

    }


    fun addTune(v: View) {
        val input = etTune.text.toString()
        var newText = ""
        for (s in input) {
            newText = if (s.isDigit() || s == '_' || s == '-' || s == '.') {
                newText + s + "  "
            } else {
                newText + s
            }
        }

        datas.add(MusicData(mutableListOf(), newText))
        refresh()
    }


    fun addLine(v: View) {
        val ft = etTune.text.toString().split("+")
        val n1 = ft[0].toInt()
        val n2 = ft[1].toInt()
        var text: String? = null
        if (ft.size >= 3) {
            text = ft[2]
        }

        datas[n1].links.add(LinkData(n2, text))

        refresh()
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

        val viewBmp = musicGrid.drawBitmap()
        val destFile = File(Environment.getExternalStorageDirectory(), UUIDUtils.uuid() + ".jpg")

        val job = async(CommonPool) {
            BitmapScaleUtil.saveBitmap(viewBmp, destFile, 100)
        }

        launch(Android) {

            val isSuccess = job.await()
            if (!isSuccess) {
                ToastUtils.systemToast(R.string.save_note_thumb_fail)
            } else {
                ToastUtils.systemToast("success")

                val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                scanIntent.data = Uri.fromFile(destFile)
                applicationContext.sendBroadcast(scanIntent)

                ImageGalleryActivity.startSelf(applicationContext, arrayListOf(destFile.absolutePath), 0)

            }
        }


    }

    private fun refresh() {
        mMusicAdapter.refresh(datas)
        musicGrid.drawMusic(datas)
    }
}
