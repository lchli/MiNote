package com.lch.menote.note.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.style.ImageSpan
import android.view.Menu
import android.view.MenuItem
import com.apkfuns.logutils.LogUtils
import com.lch.menote.R
import com.lch.menote.kotlinext.launchActivity
import com.lch.menote.note.domain.Note
import com.lch.menote.note.helper.ConstantUtil
import com.lch.menote.note.helper.NoteUtils
import com.lch.netkit.common.base.BaseCompatActivity
import kotlinx.android.synthetic.main.activity_local_note_detail.*
import kotlin.properties.Delegates

/**
 * Created by lchli on 2016/8/12.
 */

class LocalNoteDetailActivity : BaseCompatActivity() {

    private var note: Note by Delegates.notNull()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = intent.getSerializableExtra("note") as Note
        setContentView(R.layout.activity_local_note_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar!!.title = note.title

        imageEditText_content!!.movementMethod = LinkMovementMethodExt({ ms ->
            val spans = ms.getObj() as Array<Any>
            for (span in spans) {
                if (span is ImageSpan) {
                    val imagePath = String.format("%s/%s", note!!.imagesDir, span.source)
                    LogUtils.e("clicked img:" + imagePath)
                    val imgSrcs = NoteUtils.parseImageSpanSrc(imageEditText_content, ConstantUtil. STUDY_APP_ROOT_DIR)
                    var current = imgSrcs.indexOf(imagePath)
                    if (current == -1) {
                        current = 0
                    }
                    ImageGalleryActivity.startSelf(getApplicationContext(), imgSrcs, current)
                }
            }
        }, ImageSpan::class.java)
        val content = note.content


        imageEditText_content!!.text = Html.fromHtml(content, URLImageGetter(imageEditText_content), null)
        LogUtils.e("formated note.content:" + content)


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.local_note_detail_toolbar_actions, menu)
        var type = note!!.type
        val maxTypeLen = 5
        if (type.length >= maxTypeLen) {
            type = type.substring(0, maxTypeLen) + "..."
        }
       // menu.findItem(R.id.action_note_type).title = String.format("[%s]", type)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == R.id.action_edit_note) {
            //EditNoteActivity.startSelf(this, note)

            finish()

        } else if (i == android.R.id.home) {
            finish()

        }
        return super.onOptionsItemSelected(item)
    }

    companion object {


        fun startSelf(context: Context, note: Note) {
            val it = Intent(context, LocalNoteDetailActivity::class.java)
            it.putExtra("note", note)
            context.launchActivity(it)
        }
    }
}
