package com.lch.menote.common.netkit.file;


import com.lch.menote.common.netkit.file.helper.DownloadFileParams;
import com.lch.menote.common.netkit.file.helper.FileTransferListener;
import com.lch.menote.common.netkit.file.helper.FileTransferState;
import com.lch.menote.common.netkit.file.helper.NetworkError;
import com.lch.menote.common.netkit.file.helper.UploadFileParams;
import com.lch.menote.common.netkit.file.transfer.FileTransfer;
import com.lch.menote.common.netkit.file.transfer.impl.FileTransferImpl;


/**
 * 文件传输管理器。
 */
public class FileManager {


    private FileTransfer mBBTFileTransfer = null;

    public FileManager() {
        mBBTFileTransfer = new FileTransferImpl();
    }


    /**
     * 上传文件。
     *
     * @param fileParams 上传文件参数。
     * @param listener   传输监听器。
     * @return requestID 如果成功；否则返回null。
     */
    public String uploadFile(UploadFileParams fileParams, final FileTransferListener listener) {
        try {
            return chooseFileHelper(fileParams.getServerType()).uploadFile(fileParams, listener);
        } catch (final Exception e) {
            e.printStackTrace();
            FileTransfer.onError(new NetworkError(e.getMessage()), listener);
            return null;
        }
    }

    /**
     * 下载文件。
     *
     * @param fileParams 下载文件参数。
     * @param listener   传输监听器。
     * @return requestID 如果成功；否则返回null。
     */
    public String downloadFile(DownloadFileParams fileParams, final FileTransferListener listener) {
        try {
            return mBBTFileTransfer.downloadFile(fileParams, listener);
        } catch (final Exception e) {
            e.printStackTrace();
            FileTransfer.onError(new NetworkError(e.getMessage()), listener);
            return null;
        }
    }

    /**
     * 获取文件的传输状态。
     *
     * @param requestID 请求id
     * @return FileTransferState 如果成功；否则返回null。
     */
    public FileTransferState getFileTransferState(String requestID) {
        return FileTransfer.getFileTransferState(requestID);
    }


    /**
     * 取消本次传输请求。
     *
     * @param requestID 请求id
     */
    public void cancel(String requestID) {
        FileTransfer.cancel(requestID);
    }


    private FileTransfer chooseFileHelper(UploadFileParams.ServerType serverType) {
        return mBBTFileTransfer;
    }
}
