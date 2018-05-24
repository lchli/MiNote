package com.lch.menote;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class IBoxingMediaLoaderImpl implements IBoxingMediaLoader {
    @Override
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
        RequestOptions opt = RequestOptions
                .overrideOf(width, height);

        Glide.with(img.getContext())
                .load(absPath)
                .apply(opt)
                .into(img);
    }

    @Override
    public void displayRaw(@NonNull ImageView img, @NonNull String absPath, int width, int height, IBoxingCallback callback) {
        RequestOptions opt = RequestOptions
                .overrideOf(width, height);

        Glide.with(img.getContext())
                .load(absPath)
                .apply(opt)
                .into(img);

    }
}
