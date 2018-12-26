package com.lch.menote.file;

import com.lch.menote.file.datainterface.RemoteFileSource;

/**
 * Created by Administrator on 2018/12/26.
 */

public final class FileModuleInjector implements FileModuleFactory {

    private static final FileModuleInjector INS = new FileModuleInjector();

    public static FileModuleInjector getINS() {
        return INS;
    }

    private FileModuleFactory moduleFactory;

    public void initModuleFactory(FileModuleFactory factory) {
        moduleFactory = factory;
    }

    @Override
    public RemoteFileSource provideRemoteFileSource() {
        return moduleFactory.provideRemoteFileSource();
    }
}
