package com.lch.menote.note.service;

import android.text.TextUtils;

import com.lch.menote.R;
import com.lch.menote.file.FileModuleInjector;
import com.lch.menote.file.datainterface.RemoteFileSource;
import com.lch.menote.note.NoteModuleInjector;
import com.lch.menote.note.data.NetNoteRepo;
import com.lch.menote.note.datainterface.RemoteNoteSource;
import com.lch.menote.note.model.NoteElement;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.user.UserApiManager;
import com.lch.menote.user.route.User;
import com.lch.menote.utils.MvpUtils;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;
import com.lchli.utils.tool.AliJsonHelper;
import com.lchli.utils.tool.ContextProvider;
import com.lchli.utils.tool.ListUtils;

import java.io.File;
import java.util.List;

/**
 * 异步，可复用。
 * Created by lichenghang on 2018/5/19.
 */

public class CloudNoteService {


    private final RemoteNoteSource remoteNoteSource = NoteModuleInjector.getINS().provideRemoteNoteSource();
    private final RemoteFileSource remoteFileSource = FileModuleInjector.getINS().provideRemoteFileSource();

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

        new UseCase<Void, Void>() {
            @Override
            protected ResponseValue<Void> execute(Void parameters) {
                ResponseValue<Void> ret = new ResponseValue<>();

                final List<NoteElement> elms = AliJsonHelper.parseArray(note.content, NoteElement.class);
                if (ListUtils.isEmpty(elms)) {
                    ret.setErrorMsg("note content is empty.");
                    return ret;
                }

                boolean isHasFile = false;

                for (NoteElement e : elms) {
                    if (!e.type.equals(NoteElement.TYPE_TEXT)) {
                        ResponseValue<String> res = remoteFileSource.addFile(new File(e.path));
                        if (res.hasError()) {
                            ret.setErrorMsg(res.getErrorMsg());
                            return ret;
                        }
                        e.path = res.data;
                        isHasFile = true;
                    }
                }//for

                if (isHasFile) {
                    note.content = AliJsonHelper.toJSONString(elms);//reset.
                }

                final ResponseValue<Void> resSave = remoteNoteSource.save(note);

                if (resSave.hasError()) {
                    ret.setErrorMsg(resSave.getErrorMsg());
                    return ret;
                }

                return ret;

            }
        }.invokeAsync(null, cb);


    }


}
