package com.lch.menote.note.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.lch.menote.common.drawBitmap
import com.lch.menote.common.log
import com.lch.menote.common.util.AppListItemAnimatorUtils
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
    private var controllViewTotalHeight = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        musicGrid.setHasFixedSize(true)
        musicGrid.layoutManager = GridLayoutManager(this, 15)
        musicGrid.addItemDecoration(GridDividerDecoration(this, GridDividerDecoration.VERTICAL_LIST, SizeUtils.dp2px(5f)))

        mMusicAdapter = MusicAdapter(this)
        musicGrid.adapter = mMusicAdapter

    }


    fun addTune(v: View) {
        val input = etTune.text.toString()
        var newText = ""

        for (i in input.indices) {
            newText = if (i != input.length - 1) {
                newText + input[i] + "\n"
            } else {
                newText + input[i]
            }
        }

        datas.add(MusicData(mutableListOf(), newText))

        refresh()
    }


    fun addLine(v: View) {
        try {
            val ft = etTune.text.toString().split("+")
            val n1 = ft[0].toInt()
            val n2 = ft[1].toInt()
            var text: String? = null
            if (ft.size >= 3) {
                text = ft[2]
            }

            datas[n1].links.add(LinkData(n2, text))

            refresh()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteLine(v: View) {
        try {
            val ft = etTune.text.toString().split("+")
            val n1 = ft[0].toInt()
            val n2 = ft[1].toInt()

            datas[n1].links.remove(LinkData(n2))

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

    private fun refresh() {
        mMusicAdapter.refresh(datas)
        musicGrid.drawMusic(datas)
    }
}
