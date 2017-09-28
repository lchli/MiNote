package com.lch.menote.note.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.finalteam.galleryfinal.GalleryFinal
import cn.finalteam.galleryfinal.model.PhotoInfo
import cn.finalteam.toolsfinal.io.FileUtils
import com.apkfuns.logutils.LogUtils
import com.lch.menote.common.base.BaseAppCompatActivity
import com.lch.menote.common.drawBitmap
import com.lch.menote.common.util.*
import com.lch.menote.note.R
import com.lch.menote.note.data.NoteRepo
import com.lch.menote.note.domain.Note
import com.lch.menote.note.helper.BITMAP_MAX_MEMORY
import com.lch.menote.note.helper.LocalNoteListChangedEvent
import com.lch.menote.note.helper.NoteUtils
import com.lch.menote.note.helper.STUDY_APP_ROOT_DIR
import com.orhanobut.dialogplus.DialogPlus
import kotlinx.android.synthetic.main.activity_edit_note.*
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.IOException

/**
 * Created by lchli on 2016/8/12.
 */

class EditNoteActivity : BaseAppCompatActivity(), View.OnClickListener {


    private var courseUUID: String? = null
    private var courseDir: String? = null
    private var oldNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oldNote = intent.getSerializableExtra("note") as? Note
        setContentView(R.layout.activity_edit_note)
        bt_save.setOnClickListener(this)
        bt_more.setOnClickListener(this)

        imageEditText_content.movementMethod = ScrollingMovementMethod.getInstance()
        if (oldNote != null) {
            courseUUID = oldNote!!.uid
            courseDir = oldNote!!.imagesDir

            tv_note_category.setText(oldNote!!.type)
            et_note_title.setText(oldNote!!.title)
            imageEditText_content.setText(Html.fromHtml(oldNote!!.content, localImageGetter, null))
        } else {
            courseUUID = UUIDUtils.uuid()
            courseDir = NoteUtils.buildNoteDir(courseUUID!!)
            FileUtils.mkdirs(File(courseDir!!))
        }

    }


    private val localImageGetter = Html.ImageGetter { source ->
        LogUtils.e("ImageGetter thread:" + Thread.currentThread().name)

        val imagePath = "$STUDY_APP_ROOT_DIR/$source"

        val bmp = BitmapScaleUtil.decodeSampledBitmapFromPath(imagePath, BITMAP_MAX_MEMORY) ?: return@ImageGetter null

        val w = bmp.width
        val h = bmp.height

        val drawable = BitmapDrawable(bmp)
        val left = (ScreenHelper.getScreenWidth(applicationContext) - w) / 2
        drawable.setBounds(left, 0, w + left, h)
        drawable
    }

    override fun onClick(view: View) {
        val i = view.id
        if (i == R.id.bt_save) {
            val title = et_note_title.getText().toString()
            if (TextUtils.isEmpty(title)) {
                ToastUtils.systemToast(R.string.note_title_cannot_empty)
                return
            }
            val htmlContent = Html.toHtml(imageEditText_content.getText())
            if (TextUtils.isEmpty(htmlContent)) {
                ToastUtils.systemToast(R.string.note_content_cannot_empty)
                return
            }
            val thumbName = UUIDUtils.uuid() + ".jpg"
            val destFile = File(courseDir, thumbName)
            if (!destFile.exists()) {
                try {
                    destFile.createNewFile()
                } catch (e: IOException) {
                    ToastUtils.systemToast(R.string.save_note_thumb_fail)
                    e.printStackTrace()
                    return
                }

            }
            val isSuccess = BitmapScaleUtil.saveBitmap(getViewBitmap(imageEditText_content), destFile, 100)
            if (!isSuccess) {
                ToastUtils.systemToast(R.string.save_note_thumb_fail)
                return
            }
            val note = Note()
            note.type = tv_note_category.text.toString()
            note.title = title
            note.uid = courseUUID
            note.lastModifyTime = TimeUtils.getTime(System.currentTimeMillis())
            note.content = htmlContent
            note.imagesDir = courseDir
            note.thumbNail = thumbName

            NoteRepo.save(note)
            deleteUnusedImages(note)

            EventBusUtils.post(LocalNoteListChangedEvent())
            finish()

        } else if (i == R.id.bt_more) {
            val dialog = DialogPlus.newDialog(this)
                    .setAdapter(InsertImageDialogAdapter(applicationContext))
                    .setOnItemClickListener { dialog, item, view, position ->
                        when (position) {
                            0 -> {
                                dialog.dismiss()
                                openAlbum()
                            }
                            1 -> {
                                dialog.dismiss()
                                openCamera()
                            }
                        }
                    }
                    .setExpanded(true)
                    .create()
            dialog.show()

        } else if (i == R.id.tv_note_category) {
            //noteTypePop!!.showAsDropDown(title_bar)

        }
    }


    private fun openAlbum() {
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY,
                object : GalleryFinal.OnHanlderResultCallback {
                    override fun onHanlderSuccess(reqeustCode: Int, resultList: List<PhotoInfo>) {
                        if (!ListUtils.isEmpty(resultList)) {
                            val imagePath = resultList[0].photoPath
                            imageEditText_content.insertImage(imagePath, localImageGetter, courseDir!!, courseUUID!!)
                        }

                    }

                    override fun onHanlderFailure(requestCode: Int, errorMsg: String) {
                        ToastUtils.systemToast(errorMsg)
                    }
                })
    }

    private fun openCamera() {
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, object : GalleryFinal.OnHanlderResultCallback {
            override fun onHanlderSuccess(reqeustCode: Int, resultList: List<PhotoInfo>) {
                if (!ListUtils.isEmpty(resultList)) {
                    val imagePath = resultList[0].photoPath
                    LogUtils.e("gall imagePath=$imagePath")

                    imageEditText_content.insertImage(imagePath, localImageGetter, courseDir!!, courseUUID!!)
                }
            }

            override fun onHanlderFailure(requestCode: Int, errorMsg: String) {
                ToastUtils.systemToast(errorMsg)
            }
        })

    }

    override fun onBackPressed() {
        if (oldNote != null) {
            deleteUnusedImages(oldNote as Note)
        } else {
            FileUtils.deleteQuietly(File(courseDir!!))
        }
        super.onBackPressed()
    }

    private fun deleteUnusedImages(note: Note) {
        val images = File(courseDir!!).listFiles()
        if (!ArrayUtils.isEmpty(images)) {
            for (img in images) {
                if (StringUtils.equals(img.name, note.thumbNail)) {
                    continue
                }
                if (note.content == null || !note.content.contains(img.name)) {
                    img.delete()
                }

            }
        }
    }


    internal class InsertImageDialogAdapter(context: Context) : BaseAdapter() {

        private val layoutInflater: LayoutInflater

        init {
            layoutInflater = LayoutInflater.from(context)
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val viewHolder: ViewHolder
            var view: View? = convertView

            if (view == null) {
                view = layoutInflater.inflate(R.layout.list_item_insert_image_dialog, parent, false)

                viewHolder = ViewHolder()
                viewHolder.textView = view!!.findViewById(R.id.text_view) as TextView
                viewHolder.imageView = view.findViewById(R.id.image_view) as ImageView
                view.tag = viewHolder
            } else {
                viewHolder = view.tag as ViewHolder
            }

            val context = parent.context
            when (position) {
                0 -> {
                    viewHolder.textView!!.text = context.getString(R.string.photo_album)
                    viewHolder.imageView!!.setImageResource(R.drawable.ic_album)
                }
                1 -> {
                    viewHolder.textView!!.text = context.getString(R.string.camera)
                    viewHolder.imageView!!.setImageResource(R.drawable.ic_camera)
                }
            }

            return view
        }

        internal class ViewHolder {
            var textView: TextView? = null
            var imageView: ImageView? = null
        }
    }

    companion object {


        private val REQUEST_CODE_GALLERY = 1
        private val REQUEST_CODE_CAMERA = 2

        fun startSelf(context: Context) {
            val it = Intent(context, EditNoteActivity::class.java)
            if (context !is Activity) {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(it)
        }

        fun startSelf(context: Context, note: Note) {
            val it = Intent(context, EditNoteActivity::class.java)
            if (context !is Activity) {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            it.putExtra("note", note)
            context.startActivity(it)
        }

        private fun getViewBitmap(v: View): Bitmap {

            return v.drawBitmap()
        }
    }
}