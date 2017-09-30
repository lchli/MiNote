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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class UploadFileParams extends CommonParams<UploadFileParams> {

    private static final String QI_NIU_TOKEN = "QI_NIU_TOKEN";

    private UploadFileParams() {
    }

    @Override
    protected UploadFileParams thisObject() {
        return this;
    }

    public enum ServerType {
        QI_NIU,
        URL_DEFINED
    }

    private List<FileOptions> fileOptionList = new ArrayList<>(1);
    private ServerType serverType = ServerType.URL_DEFINED;

    public ServerType getServerType() {
        return serverType;
    }

    public UploadFileParams setServerType(ServerType serverType) {
        this.serverType = serverType;
        return thisObject();
    }

    public UploadFileParams addFile(FileOptions fileOptions) {
        fileOptionList.add(fileOptions);
        return thisObject();
    }

    public Iterator<FileOptions> files() {
        return fileOptionList.iterator();
    }

    public UploadFileParams setQiNiuToken(String token) {
        addParam(QI_NIU_TOKEN, token);
        return thisObject();
    }

    public String getQiNiuToken() {
        return textParam(QI_NIU_TOKEN);
    }

    public static UploadFileParams newInstance() {
        return new UploadFileParams();
    }


}
