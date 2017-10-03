package com.lch.menote.common.netkit.file.helper;


import okhttp3.Call;



public class FileTransferState {

    private Call call;
    private volatile boolean isCanceled = false;
    private double progressPercent;


    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public double getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }
}
