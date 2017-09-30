package com.lch.menote.common.netkit.file;
/*
 * Copyright (C) 2017 BabyTree-inc.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.content.Context;

import com.lch.menote.common.netkit.file.helper.DownloadFileParams;
import com.lch.menote.common.netkit.file.helper.FileTransferListener;
import com.lch.menote.common.netkit.file.helper.FileTransferState;
import com.lch.menote.common.netkit.file.helper.NetworkError;
import com.lch.menote.common.netkit.file.helper.UploadFileParams;
import com.lch.menote.common.netkit.file.transfer.FileTransfer;
import com.lch.menote.common.netkit.file.transfer.impl.BBTFileTransfer;


/**
 * 文件传输管理器。
 */
public class FileManager {


    private FileTransfer mBBTFileTransfer = null;

    public FileManager(Context context) {
        mBBTFileTransfer = new BBTFileTransfer();
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
