package com.lch.menote.share;

import android.content.Context;
import android.text.TextUtils;

import com.mob.MobSDK;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by lichenghang on 2018/6/2.
 */

public final class ShareTool {

    private static Context context;

    public static void initSdk(Context ctx) {
        context = ctx.getApplicationContext();
        MobSDK.init(ctx);
    }

    public static void showShare(String tilte, String linkUrl, String text, String imgPath) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        if (!TextUtils.isEmpty(tilte)) {
            oks.setTitle(tilte);
        }
        // titleUrl QQ和QQ空间跳转链接
        if (!TextUtils.isEmpty(linkUrl)) {
            oks.setTitleUrl(linkUrl);
        }
        // text是分享文本，所有平台都需要这个字段
        if (!TextUtils.isEmpty(text)) {
            oks.setText(text);
        }
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (!TextUtils.isEmpty(imgPath)) {
            if (imgPath.startsWith("http://") || imgPath.startsWith("https://")) {
                oks.setImageUrl(imgPath);
            } else {
                oks.setImagePath(imgPath);//确保SDcard下面存在此张图片
            }
        }
        // url在微信、微博，Facebook等平台中使用
        if (!TextUtils.isEmpty(linkUrl)) {
            oks.setUrl(linkUrl);
        }
        // comment是我对这条分享的评论，仅在人人网使用
        //oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(context);
    }
}


