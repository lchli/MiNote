package com.lch.menote.note.helper;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import cn.finalteam.galleryfinal.widget.GFImageView;


public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private static final long serialVersionUID = -7323428202724594743L;

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        RequestOptions opt = RequestOptions
                .placeholderOf(defaultDrawable)
                .override(width, height)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(activity)
                .load("file://" + path)
                .apply(opt)
                .into(imageView);
//
//                .into(new ImageViewTarget<GlideDrawable>(imageView) {
//                    @Override
//                    protected void setResource(GlideDrawable resource) {
//                        imageView.setImageDrawable(resource);
//                    }
//
//                    @Override
//                    public void setRequest(Request request) {
//                        imageView.setTag(R.id.adapter_item_tag_key,request);
//                    }
//
//                    @Override
//                    public Request getRequest() {
//                        return (Request) imageView.getTag(R.id.adapter_item_tag_key);
//                    }
//                });
    }

    @Override
    public void clearMemoryCache() {
    }
}