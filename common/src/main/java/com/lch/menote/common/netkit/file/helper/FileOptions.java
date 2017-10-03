package com.lch.menote.common.netkit.file.helper;


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
