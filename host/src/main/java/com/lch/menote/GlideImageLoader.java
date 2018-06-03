package com.lch.menote;

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

        if (!path.startsWith("http://") && !path.startsWith("https://")) {
            path = "file://" + path;
        }

        Glide.with(activity)
                .load(path)
                .apply(opt)
                .into(imageView);


    }

    @Override
    public void clearMemoryCache() {
    }
}