package com.lch.menote;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.lchli.imgloader.ImgConfig;
import com.lchli.imgloader.ImgLoaderManager;
import com.lchli.imgloader.ImgSource;

public class IBoxingMediaLoaderImpl implements IBoxingMediaLoader {
    @Override
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
        ImgLoaderManager.getINS().display(img, ImgSource.create().setImgUri(Uri.parse(absPath)), ImgConfig.create().setResizeHeight(height).setResizeWidth(width));
    }

    @Override
    public void displayRaw(@NonNull ImageView img, @NonNull String absPath, int width, int height, IBoxingCallback callback) {
        ImgLoaderManager.getINS().display(img, ImgSource.create().setImgUri(Uri.parse(absPath)), ImgConfig.create().setResizeHeight(height).setResizeWidth(width));

    }
}
