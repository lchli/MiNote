package com.lch.menote.user.domain;

import com.lch.menote.UseCase;
import com.lch.netkit.common.mvc.ResponseValue;

public class UploadFileUseCase extends UseCase<UploadFileUseCase.Params, String> {

    public static class Params {

        public String userHeadImgPath;
    }

    @Override
    protected ResponseValue<String> execute(UploadFileUseCase.Params parameters) {
        return null;
    }
}
