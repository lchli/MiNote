package com.lch.menote.common.netkit.file.helper;

/**
 * Created by bbt-team on 2017/8/10.
 */

public class FileConst {

    /*每当进度增加下载文件的10分之1时更新一下进度，避免频繁更新向UI线程发送过多Runnable阻塞UI线程。*/
    public static final float UPDATE_PROGRESS_GAP = 0.1F;

}
