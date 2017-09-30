package com.lch.menote.common.netkit.file.helper;

/**
 * Created by bbt-team on 2017/8/3.
 */

public class DownloadFileParams extends CommonParams<DownloadFileParams> {

    private String saveDir;

    private DownloadFileParams() {
    }


    public String getSaveDir() {
        return saveDir;
    }

    public DownloadFileParams setSaveDir(String saveDir) {
        this.saveDir = saveDir;
        return this;
    }


    public static DownloadFileParams newInstance() {
        return new DownloadFileParams();
    }

    @Override
    protected DownloadFileParams thisObject() {
        return this;
    }
}
