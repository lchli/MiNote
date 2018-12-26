package com.lch.menote.file;

import com.lch.menote.file.datainterface.RemoteFileSource;

/**
 * Created by Administrator on 2018/12/26.
 */

public interface FileModuleFactory {

    RemoteFileSource provideRemoteFileSource();
}
