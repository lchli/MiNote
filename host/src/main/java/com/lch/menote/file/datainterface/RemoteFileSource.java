package com.lch.menote.file.datainterface;


import com.lchli.arch.clean.ResponseValue;

import java.io.File;

public interface RemoteFileSource {

    ResponseValue<String> addFile(File f);
}
