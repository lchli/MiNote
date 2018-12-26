package com.lch.menote.file.dataImpl;

import android.text.TextUtils;

import com.lch.menote.ApiConstants;
import com.lch.menote.file.datainterface.RemoteFileSource;
import com.lch.menote.note.domain.response.UploadFileResponse;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.v2.NetKit;
import com.lch.netkit.v2.common.NetworkResponse;
import com.lch.netkit.v2.filerequest.FileOptions;
import com.lch.netkit.v2.filerequest.UploadFileParams;
import com.lch.netkit.v2.parser.Parser;

import java.io.File;

public class NetFileSource implements RemoteFileSource {

    @Override
    public ResponseValue<String> addFile(File f) {
        ResponseValue<String> ret = new ResponseValue<>();

        UploadFileParams param = RequestUtils.minoteUploadFileParams()
                .setUrl(ApiConstants.UPLOAD_FILE)
                .addFile(new FileOptions().setFileKey("file").setFile(f));

        NetworkResponse<UploadFileResponse> resf = NetKit.fileRequest().syncUploadFile(param, new Parser<UploadFileResponse>() {
            @Override
            public UploadFileResponse parse(String s) {
                return AliJsonHelper.parseObject(s, UploadFileResponse.class);
            }
        });

        if (resf.hasError() || resf.data == null || TextUtils.isEmpty(resf.data.data)) {
            ret.setErrorMsg("file upload fail.");
            return ret;
        }

        ret.data = resf.data.data;

        return ret;

    }
}
