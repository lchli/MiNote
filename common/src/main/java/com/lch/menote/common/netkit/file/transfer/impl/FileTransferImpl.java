package com.lch.menote.common.netkit.file.transfer.impl;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.lch.menote.common.netkit.NetClient;
import com.lch.menote.common.netkit.file.helper.DownloadFileParams;
import com.lch.menote.common.netkit.file.helper.FileConst;
import com.lch.menote.common.netkit.file.helper.FileOptions;
import com.lch.menote.common.netkit.file.helper.FileResponse;
import com.lch.menote.common.netkit.file.helper.FileTransferListener;
import com.lch.menote.common.netkit.file.helper.FileTransferState;
import com.lch.menote.common.netkit.file.helper.NetworkError;
import com.lch.menote.common.netkit.file.helper.UploadFileParams;
import com.lch.menote.common.netkit.file.transfer.FileTransfer;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 上传地址由url指定的文件传输器实现。
 */
public class FileTransferImpl extends FileTransfer {

    private static final String TAG = "FileTransferImpl";
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
    private static final int DOWNLOAD_BUF = 1024;

    private final ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));

    private final OkHttpClient mOkHttpClient = NetClient.ok();


    @Override
    public String uploadFile(final UploadFileParams fileParams, final FileTransferListener lsn) {

        final FileTransferListener listener = lsn != null ? lsn : FileTransferListener.DEF_LISTENER;

        if (fileParams == null) {
            onError(new NetworkError("fileParams is null."), listener);
            return null;
        }
        final Iterator<FileOptions> filesIter = fileParams.files();

        if (TextUtils.isEmpty(fileParams.getUrl())) {
            onError(new NetworkError("uploadUrl is empty."), listener);
            return null;
        }
        final String requestID = UUID.randomUUID().toString();

        final FileTransferState state = new FileTransferState();
        calls.put(requestID, state);

        runAsync(new Runnable() {
            @Override
            public void run() {

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);

                Iterator<Map.Entry<String, String>> textParamsIter = fileParams.textParams();
                while (textParamsIter.hasNext()) {
                    Map.Entry<String, String> item = textParamsIter.next();
                    builder.addFormDataPart(item.getKey(), item.getValue());
                }

                while (filesIter.hasNext()) {
                    FileOptions fileOpt = filesIter.next();
                    if (fileOpt.getFile() != null) {
                        builder.addFormDataPart(fileOpt.getFileKey(), fileOpt.getFileName(), RequestBody.create(MEDIA_TYPE_STREAM, fileOpt.getFile()));
                    } else if (fileOpt.getFilePath() != null) {
                        builder.addFormDataPart(fileOpt.getFileKey(), fileOpt.getFileName(), RequestBody.create(MEDIA_TYPE_STREAM, new File(fileOpt.getFilePath())));
                    } else if (fileOpt.getFileBytes() != null) {
                        builder.addFormDataPart(fileOpt.getFileKey(), fileOpt.getFileName(), RequestBody.create(MEDIA_TYPE_STREAM, fileOpt.getFileBytes()));
                    }
                }

                Request.Builder requestBuilder = new Request.Builder();

                Iterator<Map.Entry<String, String>> headers = fileParams.headers();
                while (headers.hasNext()) {
                    Map.Entry<String, String> header = headers.next();
                    requestBuilder.addHeader(header.getKey(), header.getValue());
                }

                RequestBody requestBody = builder.build();

                Request request = requestBuilder
                        .url(fileParams.getUrl())
                        .post(new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
                            float previousPercent = 0;

                            @Override
                            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                                float percent = formatPercent(bytesWritten, contentLength);
                                state.setProgressPercent(percent);

                                if (bytesWritten < contentLength && (percent - previousPercent) < FileConst.UPDATE_PROGRESS_GAP) {
                                    return;
                                }
                                previousPercent = percent;
                                onProgress(percent, listener);
                            }
                        }))
                        .build();

                final Call call = mOkHttpClient.newCall(request);
                state.setCall(call);

                try {
                    if (state.isCanceled()) {
                        onError(new NetworkError("canceled!"), listener);
                        return;
                    }

                    final Response response = call.execute();
                    if (!response.isSuccessful()) {
                        onError(new NetworkError(response.code(), response.message()), listener);
                        return;
                    }
                    ResponseBody body = response.body();
                    if (body == null) {
                        onError(new NetworkError(response.code(), "response body is null"), listener);
                        return;
                    }
                    FileResponse respondData = new FileResponse();
                    respondData.setReponseString(body.string());

                    onResponse(respondData, listener);

                } catch (final Exception e) {
                    e.printStackTrace();
                    onError(new NetworkError(e.getMessage()), listener);
                } finally {
                    calls.remove(requestID);
                }

            }
        });

        return requestID;
    }

    @Override
    public String downloadFile(final DownloadFileParams fileParams, final FileTransferListener lsn) {

        final FileTransferListener listener = lsn != null ? lsn : FileTransferListener.DEF_LISTENER;

        if (fileParams == null) {
            onError(new NetworkError("fileParams is null."), listener);
            return null;
        }
        if (TextUtils.isEmpty(fileParams.getUrl())) {
            onError(new NetworkError("file url is empty."), listener);
            return null;
        }
        if (TextUtils.isEmpty(fileParams.getSaveDir())) {
            onError(new NetworkError("file save dir is invalid."), listener);
            return null;
        }
        final String requestID = EncryptUtils.encryptMD5ToString(fileParams.getUrl());

        FileTransferState stateOld = getFileTransferState(requestID);
        if (stateOld != null) {
            onError(new NetworkError("file is already downloading."), listener);
            return requestID;
        }

        final FileTransferState state = new FileTransferState();
        calls.put(requestID, state);

        runAsync(new Runnable() {
            @Override
            public void run() {

                Request.Builder requestBuilder = new Request.Builder();

                Iterator<Map.Entry<String, String>> headers = fileParams.headers();
                while (headers.hasNext()) {
                    Map.Entry<String, String> header = headers.next();
                    requestBuilder.addHeader(header.getKey(), header.getValue());
                }

                InputStream is = null;
                FileOutputStream fos = null;

                try {
                    long totalLen = getUrlContentLength(fileParams.getUrl());
                    log("totalLen=%d", totalLen);

                    final String saveFileName = EncryptUtils.encryptMD5ToString(fileParams.getUrl()) + getNameFromUrl(fileParams.getUrl());
                    createFileIfNotExist(fileParams.getSaveDir());
                    final File saveFile = createFileIfNotExist(fileParams.getSaveDir() + "/" + saveFileName);
                    log("saveFile=%s", saveFile.getAbsolutePath());

                    long downloadedLength = saveFile.length();
                    log("downloadLength=%d", downloadedLength);

                    boolean append;
                    if (totalLen == -1 || downloadedLength >= totalLen) {
                        log("do not use append mode download,url=%s", fileParams.getUrl());
                        append = false;
                        downloadedLength = 0;
                    } else {//支持断点。
                        log("use append mode download,url=%s", fileParams.getUrl());
                        append = true;
                        requestBuilder.addHeader("RANGE", "bytes=" + downloadedLength + "-");
                    }

                    Request request = requestBuilder.get()
                            .url(fileParams.getUrl())
                            .build();
                    final Call call = mOkHttpClient.newCall(request);
                    state.setCall(call);

                    if (state.isCanceled()) {
                        onError(new NetworkError("canceled!"), listener);
                        return;
                    }

                    final Response response = call.execute();

                    if (!response.isSuccessful()) {
                        onError(new NetworkError(response.code(), response.message()), listener);
                        return;
                    }

                    ResponseBody body = response.body();
                    if (body == null) {
                        onError(new NetworkError(response.code(), "response body is null"), listener);
                        return;
                    }

                    fos = new FileOutputStream(saveFile, append);
                    is = body.byteStream();

                    byte[] buf = new byte[DOWNLOAD_BUF];
                    float previousPercent = 0;
                    long sum = downloadedLength;
                    int len;

                    sendProgress(sum, totalLen, state, listener);

                    while ((len = is.read(buf)) != -1) {
                        sum += len;
                        fos.write(buf, 0, len);

                        float percent = formatPercent(sum, totalLen);
                        state.setProgressPercent(percent);

                        if (percent - previousPercent < FileConst.UPDATE_PROGRESS_GAP) {
                            continue;
                        }
                        previousPercent = percent;

                        onProgress(percent, listener);//只在增量大于总长度一定比例后才通知UI更新，减少UI线程的丢帧率。
                    }

                    sendProgress(sum, totalLen, state, listener);

                    fos.flush();

                    FileResponse respondData = new FileResponse();
                    respondData.setReponseFile(saveFile);

                    onResponse(respondData, listener);

                } catch (final Exception e) {
                    e.printStackTrace();
                    onError(new NetworkError(e.getMessage()), listener);

                } finally {
                    IOUtils.closeQuietly(is, fos);
                    calls.remove(requestID);
                }
            }
        });

        return requestID;

    }

    private void runAsync(Runnable runnable) {
        executorService.execute(runnable);
    }

    private void sendProgress(long currentBytes, long totalBytes, FileTransferState state, FileTransferListener listener) {
        float percent = formatPercent(currentBytes, totalBytes);
        state.setProgressPercent(percent);
        onProgress(percent, listener);
    }

    private static String getNameFromUrl(@NonNull String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    private long getUrlContentLength(String url) throws Exception {
        Request request = new Request.Builder()
                .head()
                .url(url)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            throw new IOException("can not get Content-Length url=" + url);
        }
        long len = body.contentLength();
        body.close();
        return len;
    }


    private static File createFileIfNotExist(String path) throws IOException {
        final File file = new File(path);
        if (file.isDirectory()) {
            file.mkdirs();
        } else if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    private static float formatPercent(long currentBytes, long totalBytes) {
        if (totalBytes == -1) {
            return 0.5F;
        }
        return (float) currentBytes / (float) totalBytes;
    }

    private static void log(String format, Object... args) {
        log(TAG, String.format(format, args));
    }


    private static class CountingRequestBody extends RequestBody {

        private RequestBody delegate;
        private Listener listener;
        private CountingSink countingSink;

        private CountingRequestBody(RequestBody delegate, Listener listener) {
            this.delegate = delegate;
            this.listener = listener;
        }


        @Override

        public MediaType contentType() {
            return delegate.contentType();
        }


        @Override
        public long contentLength() {
            try {
                return delegate.contentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;

        }


        @Override

        public void writeTo(BufferedSink sink) throws IOException {
            countingSink = new CountingSink(sink);
            BufferedSink bufferedSink = Okio.buffer(countingSink);
            delegate.writeTo(bufferedSink);
            bufferedSink.flush();
        }


        private final class CountingSink extends ForwardingSink {

            private long bytesWritten = 0;

            public CountingSink(Sink delegate) {
                super(delegate);
            }


            @Override

            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                bytesWritten += byteCount;
                listener.onRequestProgress(bytesWritten, contentLength());
            }


        }

        private interface Listener {
            void onRequestProgress(long bytesWritten, long contentLength);
        }


    }


}
