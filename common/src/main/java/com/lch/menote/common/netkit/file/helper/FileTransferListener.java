package com.lch.menote.common.netkit.file.helper;
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

/**
 * 文件传输回调。
 * Created by liyonghao on 27/07/2017.
 */

public interface FileTransferListener {

    void onResponse(FileResponse response);

    void onError(NetworkError error);

    void onProgress(double percent);


    FileTransferListener DEF_LISTENER = new FileTransferListener() {
        @Override
        public void onResponse(FileResponse response) {

        }

        @Override
        public void onError(NetworkError error) {

        }

        @Override
        public void onProgress(double percent) {

        }
    };
}
