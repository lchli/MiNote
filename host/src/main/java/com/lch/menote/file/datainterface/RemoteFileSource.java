package com.lch.menote.file.datainterface;

import com.lch.netkit.common.mvc.ResponseValue;

import java.io.File;

public interface RemoteFileSource {

    ResponseValue<String> addFile(File f);
}
