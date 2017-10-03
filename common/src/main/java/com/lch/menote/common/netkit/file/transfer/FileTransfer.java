package com.lch.menote.common.netkit.file.transfer;


import com.lch.menote.common.netkit.file.helper.DownloadFileParams;
import com.lch.menote.common.netkit.file.helper.FileResponse;
import com.lch.menote.common.netkit.file.helper.FileTransferListener;
import com.lch.menote.common.netkit.file.helper.FileTransferState;
import com.lch.menote.common.netkit.file.helper.NetworkError;
import com.lch.menote.common.netkit.file.helper.UploadFileParams;
import com.lch.menote.common.util.UiThread;

import java.util.HashMap;
import java.util.Map;

public abstract class FileTransfer {

    protected static final Map<String, FileTransferState> calls = new HashMap<>();

    public static void cancel(String requestID) {
        if (requestID == null) {
            return;
        }

        FileTransferState state = calls.get(requestID);
        if (state != null) {
            if (state.getCall() != null) {
                state.getCall().cancel();
            }
            state.setCanceled(true);
        }
    }

    public static FileTransferState getFileTransferState(String requestID) {
        if (requestID == null) {
            return null;
        }
        return calls.get(requestID);
    }

    public static void onError(final NetworkError error, final FileTransferListener listener) {
        UiThread.run(new Runnable() {
            @Override
            public void run() {
                listener.onError(error);
            }
        });
    }

    public static void onResponse(final FileResponse data, final FileTransferListener listener) {
        UiThread.run(new Runnable() {
            @Override
            public void run() {
                listener.onResponse(data);
            }
        });
    }

    public static void onProgress(final double percent, final FileTransferListener listener) {
        UiThread.run(new Runnable() {
            @Override
            public void run() {
                listener.onProgress(percent);
            }
        });
    }

    public abstract String uploadFile(UploadFileParams fileParams, final FileTransferListener listener);

    public abstract String downloadFile(DownloadFileParams fileParams, final FileTransferListener listener);
}
