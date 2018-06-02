package com.lch.menote.note.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bumptech.glide.Glide
import com.lch.menote.R
import com.lch.netkit.common.base.BaseCompatActivity
import kotlinx.android.synthetic.main.activity_image_gallery.*
import java.util.*

/**
 * Created by lchli on 2016/8/13.
 */

class ImageGalleryActivity : BaseCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_gallery)

        val images: List<String> = getIntent().getStringArrayListExtra("images") as List<String>
        val current = getIntent().getIntExtra("current", 0)

        val creator = CBViewHolderCreator<LocalImageHolderView> { LocalImageHolderView() }

        (convenientBanner as ConvenientBanner<String>).setPages(creator, images)
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)

        convenientBanner!!.setcurrentitem(current)

    }

    private inner class LocalImageHolderView : Holder<String> {

        private var imageView: ImageView? = null

        override fun createView(context: Context): View {
            imageView = ImageView(context)
            imageView!!.scaleType = ImageView.ScaleType.FIT_CENTER
            return imageView as View
        }

        override fun UpdateUI(context: Context, position: Int, path: String) {
            Glide.with(context).load(path).into(imageView)
        }
    }

    companion object {

        fun startSelf(context: Context, images: ArrayList<String>, current: Int) {
            val it = Intent(context, ImageGalleryActivity::class.java)
            if (context !is Activity) {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            it.putStringArrayListExtra("images", images)
            it.putExtra("current", current)
            context.startActivity(it)
        }
    }
}
