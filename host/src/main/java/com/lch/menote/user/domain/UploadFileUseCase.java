package com.lch.menote.user.domain;

import com.lch.menote.file.datainterface.RemoteFileSource;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

import java.io.File;

public class UploadFileUseCase extends UseCase<UploadFileUseCase.Params, String> {

    public static class Params {

        public File userHeadImg;
    }

    private RemoteFileSource fileSource;

    public UploadFileUseCase(RemoteFileSource fileSource) {
        this.fileSource = fileSource;
    }

    @Override
    protected ResponseValue<String> execute(UploadFileUseCase.Params parameters) {
        return fileSource.addFile(parameters.userHeadImg);
    }
}
