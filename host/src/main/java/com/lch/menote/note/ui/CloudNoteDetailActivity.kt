package com.lch.menote.note.ui

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.apkfuns.logutils.LogUtils
import com.lch.menote.R
import com.lch.menote.note.domain.NoteModel
import com.lch.netkit.common.base.BaseCompatActivity
import kotlinx.android.synthetic.main.activity_cloud_note_detail.*

class CloudNoteDetailActivity : BaseCompatActivity() {

    private var note: NoteModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        note = intent.getSerializableExtra("note") as NoteModel

        setContentView(R.layout.activity_cloud_note_detail)


        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar.setTitle(note!!.title)


        LogUtils.e("note.content:" + note!!.content)
        LogUtils.e("note.content:" + note!!.ShareUrl)

        loadUserAsync()

        web.getSettings().setSupportZoom(false)

        web.getSettings().setBuiltInZoomControls(false)

        web.setWebViewClient(WebClient())


       web.loadUrl(note!!.ShareUrl)
       // web.loadData(note!!.content,"text/html","utf-8")

    }


    private fun loadUserAsync() {

//        UserController.
//
//        val job = async(CommonPool) {
//            try {
//                val mod = RouteCall.getUserModule()?: throw Exception("can not find user module")
//
//                mod.queryUser(note!!.userId) ?: throw Exception("userId not found")
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                e
//            }
//
//        }
//
//        launch(Android) {
//            val e = job.await()
//
//            if (e is Exception) {
//                applicationContext.toast(e.message)
//
//            } else {
//                userNick.text = (e as User).userName
//            }
//
//
//        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.cloud_note_detail_toolbar_actions, menu)

        var type: String? = note!!.type


        val maxTypeLen = 5

        if (type != null && type.length >= maxTypeLen) {

            type = type.substring(0, maxTypeLen) + "..."

        }

        //menu.findItem(R.id.action_note_type).title = String.format("[%s]", type)

        return true

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home ->

                finish()
        }

        return super.onOptionsItemSelected(item)

    }


    override fun onBackPressed() {

        if (web.canGoBack()) {
            web.goBack()
            return
        }

        super.onBackPressed()

    }


    private class WebClient : WebViewClient() {


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {

            view.loadUrl(request.url.toString())

            return true

        }


        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            view.loadUrl(url)

            return true

        }

    }

    companion object {


        fun startSelf(context: Context, note: NoteModel) {

            val it = Intent(context, CloudNoteDetailActivity::class.java)

            if (context !is Activity) {

                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            }

            it.putExtra("note", note)

            context.startActivity(it)

        }
    }


}