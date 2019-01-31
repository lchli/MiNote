package com.lch.menote.note.service;

import android.text.TextUtils;

import com.lch.menote.ApiConstants;
import com.lch.menote.R;
import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.data.NetNoteRepo;
import com.lch.menote.note.data.response.UploadFileResponse;
import com.lch.menote.note.datainterface.RemoteNoteSource;
import com.lch.menote.note.model.NoteElement;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.user.UserApiManager;
import com.lch.menote.user.route.User;
import com.lch.menote.utils.MvpUtils;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.v2.NetKit;
import com.lch.netkit.v2.common.NetworkResponse;
import com.lch.netkit.v2.filerequest.FileOptions;
import com.lch.netkit.v2.filerequest.UploadFileParams;
import com.lch.netkit.v2.parser.Parser;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;
import com.lchli.utils.tool.AliJsonHelper;
import com.lchli.utils.tool.ContextProvider;
import com.lchli.utils.tool.ListUtils;
import com.lchli.utils.tool.TaskExecutor;

import java.util.List;

/**
 * 异步，可复用。
 * Created by lichenghang on 2018/5/19.
 */

public class CloudNoteService {


    private final RemoteNoteSource remoteNoteSource = NoteModuleInjector.getINS().provideRemoteNoteSource();

    public void getCloudNotes(final NetNoteRepo.NetNoteQuery query, ControllerCallback<List<NoteModel>> cb) {
        new UseCase<Void, List<NoteModel>>() {
            @Override
            protected ResponseValue<List<NoteModel>> execute(Void parameters) {
                return remoteNoteSource.queryNotes(query);
            }
        }.invokeAsync(null, cb);
    }


    public void deleteNote(final String noteId, final ControllerCallback<Void> cb) {
        new UseCase<Void, Void>() {
            @Override
            protected ResponseValue<Void> execute(Void parameters) {
                return remoteNoteSource.delete(noteId);
            }
        }.invokeAsync(null, cb);

    }

    public void updateNetNote(final NoteModel note, final ControllerCallback<Void> cb) {
        saveNoteToNetImpl(note, cb);
    }


    public void uploadNote(final NoteModel note, final ControllerCallback<Void> cb) {
        note.uid = null;//server will generate uid.
        saveNoteToNetImpl(note, cb);
    }

    public void publicNetNote(final String noteId, final ControllerCallback<NoteModel> cb) {
        new UseCase<Void, NoteModel>() {
            @Override
            protected ResponseValue<NoteModel> execute(Void parameters) {
                return remoteNoteSource.publicNote(noteId);
            }
        }.invokeAsync(null, cb);
    }

    public void likeNetNote(final String noteId, final ControllerCallback<NoteModel> cb) {
        new UseCase<Void, NoteModel>() {
            @Override
            protected ResponseValue<NoteModel> execute(Void parameters) {
                return remoteNoteSource.likeNote(noteId);
            }
        }.invokeAsync(null, cb);
    }


    private void saveNoteToNetImpl(final NoteModel note, final ControllerCallback<Void> callback) {
        final ControllerCallback<Void> cb = MvpUtils.newUiThreadProxy(callback);

        if (note == null) {
            cb.onError(0, "note is null.");
            return;
        }
        final User se = UserApiManager.getINS().getSession();

        if (se == null || TextUtils.isEmpty(se.uid) || TextUtils.isEmpty(se.token)) {
            cb.onError(0, ContextProvider.context().getString(R.string.not_login));
            return;
        }

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<NoteElement> elms = AliJsonHelper.parseArray(note.content, NoteElement.class);
                if (ListUtils.isEmpty(elms)) {
                    cb.onError(0, "note content is empty.");
                    return;
                }


                boolean isHasFile = false;

                for (NoteElement e : elms) {
                    if (!e.type.equals(NoteElement.TYPE_TEXT)) {
                        UploadFileParams param = RequestUtils.minoteUploadFileParams()
                                .addParam("userId", se.uid)
                                .addParam("userToken", se.token)
                                .setUrl(ApiConstants.UPLOAD_FILE)
                                .addFile(new FileOptions().setFileKey("file").setFilePath(e.path));

                        final NetworkResponse<UploadFileResponse> res = NetKit.fileRequest().syncUploadFile(param, new Parser<UploadFileResponse>() {
                            @Override
                            public UploadFileResponse parse(String s) {
                                return AliJsonHelper.parseObject(s, UploadFileResponse.class);
                            }
                        });
                        if (res.hasError()) {
                            cb.onError(0, res.getErrorMsg());
                            return;
                        }

                        if (res.data == null) {
                            cb.onError(0, "res data is null");
                            return;
                        }

                        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
                            cb.onError(0, res.data.message);
                            return;
                        }


                        e.path = res.data.data;
                        isHasFile = true;
                    }
                }//for


                if (isHasFile) {
                    note.content = AliJsonHelper.toJSONString(elms);
                }

                final ResponseValue<Void> resSave = remoteNoteSource.save(note);

                if (resSave.hasError()) {
                    cb.onError(0, resSave.getErrorMsg());
                    return;
                }

                cb.onSuccess(resSave.data);

            }
        });

    }


}
