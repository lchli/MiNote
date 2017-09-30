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

import java.io.File;
import java.util.UUID;

public class FileOptions {

    private String filePath;
    private byte[] fileBytes;
    private File file;
    private String fileKey;

    public String getFilePath() {
        return filePath;
    }

    public FileOptions setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public FileOptions setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
        return this;
    }

    public File getFile() {
        return file;
    }

    public FileOptions setFile(File file) {
        this.file = file;
        return this;
    }

    public String getFileKey() {
        return fileKey;
    }

    public FileOptions setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public String getFileName() {
        if (file != null) {
            return file.getName();
        }
        if (filePath != null) {
            return new File(filePath).getName();
        }
        return UUID.randomUUID().toString();
    }
}
