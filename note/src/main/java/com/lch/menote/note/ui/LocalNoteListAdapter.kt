package com.lch.menote.note.ui

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.lch.menote.common.Glo
import com.lch.menote.common.log
import com.lch.menote.common.netkit.file.helper.*
import com.lch.menote.common.route.UserMod
import com.lch.menote.common.toast
import com.lch.menote.common.util.*
import com.lch.menote.note.R
import com.lch.menote.note.data.DataSources
import com.lch.menote.note.data.NoteRepo
import com.lch.menote.note.domain.*
import com.lch.menote.note.helper.UrlConst
import com.lch.menote.note.helper.VIEW_TYPE_HEADER
import com.lch.menote.note.helper.VIEW_TYPE_ITEM
import com.lch.menote.note.helper.VIEW_TYPE_PINED
import com.lch.route.noaop.lib.RouteEngine
import com.lchli.pinedrecyclerlistview.library.ListSectionData
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerAdapter
import com.orhanobut.dialogplus.DialogPlus
import kotlinx.android.synthetic.main.local_note_list_header.view.*
import kotlinx.android.synthetic.main.local_note_list_item.view.*
import kotlinx.android.synthetic.main.local_note_list_pined_item.view.*
import org.apache.commons.io.FileUtils
import java.io.File


class LocalNoteListAdapter : PinnedRecyclerAdapter() {


    private val def = BitmapFactory.decodeResource(ContextProvider.context().resources,

            R.drawable.ic_add_note)


    override fun getItemViewType(position: Int): Int {

        val `object` = mDatas[position]

        if (`object` is ListSectionData) {

            return `object`.sectionViewType

        }

        return if (`object` is HeadData) {

            VIEW_TYPE_HEADER

        } else VIEW_TYPE_ITEM


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View



        when (viewType) {

            VIEW_TYPE_HEADER -> {

                view = LayoutInflater.from(parent.context)

                        .inflate(R.layout.local_note_list_header, parent, false)

                return HeaderViewHolder(view)
            }

            VIEW_TYPE_PINED -> {

                view = LayoutInflater.from(parent.context)

                        .inflate(R.layout.local_note_list_pined_item, parent, false)

                return PinedViewHolder(view)
            }


            else -> {

                view = LayoutInflater.from(parent.context)

                        .inflate(R.layout.local_note_list_item, parent, false)

                return ViewHolder(view)
            }
        }


    }


    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {

        val viewtype = getItemViewType(position)

        val o = getItem(position)



        if (viewtype == VIEW_TYPE_PINED) {

            val holder = h as PinedViewHolder

            val pinedData = o as NotePinedData

            holder.pinedItem.pinedHeader!!.setText(pinedData.noteType + "")

            return

        }



        if (viewtype == VIEW_TYPE_HEADER) {

            val holder = h as HeaderViewHolder
            holder.headerItem.imageView.setImageResource(R.drawable.ic_add_note)
            return

        }


        val holder = h as ViewHolder

        val data = o as Note
        val context = holder.listItem.context

        holder.listItem.couse_title_textView!!.setText(data.title)

        holder.listItem.course_time_textView!!.setText(data.lastModifyTime)

        if (!isScrolling) {
            with(holder.listItem) {
                if (data.category == Note.CAT_MUSIC) {
                    Glide.with(getContext()).load(R.drawable.music).override(SizeUtils.dp2px(100f), SizeUtils.dp2px(100f)).into(course_thumb_imageView)
                } else {
                    Glide.with(getContext()).load(R.drawable.app_logo).override(SizeUtils.dp2px(100f), SizeUtils.dp2px(100f)).into(course_thumb_imageView)
                }

            }

        } else {
            holder.listItem.course_thumb_imageView!!.setImageBitmap(def)
        }
        holder.listItem.setOnClickListener {
            if (data.category == Note.CAT_MUSIC) {
                MusicActivity.launch(context, data)
            } else {
                LocalNoteDetailActivity.startSelf(context, data)
            }
        }


        holder.listItem.setOnLongClickListener {
            val adp = ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1)
            adp.add("删除")

            val dialog = DialogPlus.newDialog(context)
                    .setAdapter(adp)
                    .setOnItemClickListener { dialog, item, view, position ->
                        when (position) {
                            0 -> {
                                dialog.dismiss()

                                NoteRepo(DataSources.localNote).delete(data)
                                FileUtils.deleteQuietly(File(data.imagesDir))
                                EventBusUtils.post(LocalNoteListChangedEvent())
                            }

                        }
                    }
                    .setExpanded(false)
                    .create()
            dialog.show()
            true
        }

        holder.listItem.course_upload.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {

                val mod = RouteEngine.getModule(UserMod.MODULE_NAME) as? UserMod
                if (mod == null) {
                    context.toast("user module is null")
                    return
                }

                val userId = mod.userId()
                if (userId == null) {
                    context.toast("not login")
                    return
                }


                val files = File(data.imagesDir).listFiles()

                val uploadFileParams = UploadFileParams.newInstance().setUrl(UrlConst.UPLOAD_NOTE_URL)
                        .addParam("Uid", data.uid)
                        .addParam("Title", data.title)
                        .addParam("ImagesDir", data.imagesDir)
                        .addParam("LastModifyTime", data.lastModifyTime)
                        .addParam("Type", data.type)
                        .addParam("ThumbNail", data.thumbNail)
                        .addParam("Content", data.content)
                        .addParam("UserId", userId)

                if (files != null) {

                    for (f in files) {
                        uploadFileParams.addFile(FileOptions().setFile(f).setFileKey("images"))
                    }

                }


                Glo.fileManager.uploadFile(uploadFileParams, object : FileTransferListener {
                    override fun onResponse(response: FileResponse?) {

                        try {
                            context.log(response!!.reponseString)

                            val resp = Gson().fromJson(response!!.reponseString, BaseResponse::class.java)
                            if (resp.code == 0) {
                                context.toast(ResUtils.parseString(R.string.upload_note_success))

                                EventBusUtils.post(CloudNoteListChangedEvent())
                            } else {
                                context.toast(resp.msg)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            context.toast(e.message)
                        }

                    }

                    override fun onError(error: NetworkError?) {
                        ToastUtils.systemToast(error!!.msg)
                    }

                    override fun onProgress(percent: Double) {
                    }
                })

            }
        })



        AppListItemAnimatorUtils.startAnim(holder.listItem)


    }


    internal inner class ViewHolder(var listItem: View) : RecyclerView.ViewHolder(listItem)


    internal inner class HeaderViewHolder(var headerItem: View) : RecyclerView.ViewHolder(headerItem)


    internal inner class PinedViewHolder(var pinedItem: View) : RecyclerView.ViewHolder(pinedItem)


}