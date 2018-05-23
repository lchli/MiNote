package com.lch.menote.note.controller;

import android.content.Context;

import com.lch.menote.common.util.AliJsonHelper;
import com.lch.menote.common.util.ListUtils;
import com.lch.menote.common.util.UiHandler;
import com.lch.menote.note.TaskExecutor;
import com.lch.menote.note.data.NoteSource;
import com.lch.menote.note.data.db.DbNoteRepo;
import com.lch.menote.note.data.net.NetNoteRepo;
import com.lch.menote.note.domain.HeadData;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteElement;
import com.lch.menote.note.domain.NotePinedData;
import com.lch.menote.note.helper.ConstantUtil;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.MvcError;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.file.helper.FileOptions;
import com.lch.netkit.file.helper.FileResponse;
import com.lch.netkit.file.helper.FileTransferListener;
import com.lch.netkit.file.helper.UploadFileParams;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class NoteController {


    private NoteSource localNoteSource;
    private NoteSource netNoteSource;

    public NoteController(Context context) {
        localNoteSource = new DbNoteRepo(context);
        netNoteSource = new NetNoteRepo();
    }


    public void getLocalNotesWithCat(final String tag, final String title, final boolean sortTimeAsc, final String useId, final ControllerCallback<List<Object>> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<List<Object>> r = getNotesWithCatImpl(tag, title, sortTimeAsc, useId, localNoteSource);
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(r);
                        }
                    }
                });

            }
        });
    }

    private ResponseValue<List<Object>> getNotesWithCatImpl(String tag, String title, boolean sortTimeAsc, String useId, NoteSource source) {
        ResponseValue<List<Object>> res = new ResponseValue<>();

        ResponseValue<List<Note>> notesRes = source.queryNotes(tag, title, sortTimeAsc, useId);
        if (notesRes.hasError()) {
            res.err = notesRes.err;
            return res;
        }

        List<Note> notes = notesRes.data;
        if (notes == null || notes.isEmpty()) {
            return res;
        }

        List<Object> all = new ArrayList<>();
        res.data = all;

        all.add(new HeadData());


        String preType = "";

        for (Note note : notes) {
            String currentType = note.type;
            if (!preType.equals(currentType)) {
                all.add(new NotePinedData(ConstantUtil.VIEW_TYPE_PINED, currentType));
                preType = currentType;
            }

            all.add(note);
        }

        return res;
    }

    public void getLocalNotes(final String tag, final String title, final boolean sortTimeAsc, final String useId, final ControllerCallback<List<Note>> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<List<Note>> r = localNoteSource.queryNotes(tag, title, sortTimeAsc, useId);
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(r);
                        }
                    }
                });

            }
        });

    }

    public void getCloudNotes(final String tag, final String title, final boolean sortTimeAsc, final String useId, final ControllerCallback<List<Note>> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<List<Note>> r = netNoteSource.queryNotes(tag, title, sortTimeAsc, useId);
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(r);
                        }
                    }
                });

            }
        });

    }

    public void getCloudNotesWithCat(final String tag, final String title, final boolean sortTimeAsc, final String useId, final ControllerCallback<List<Object>> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<List<Object>> r = getNotesWithCatImpl(tag, title, sortTimeAsc, useId, netNoteSource);
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(r);
                        }
                    }
                });

            }
        });
    }


    public void saveLocalNote(final Note note, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                localNoteSource.save(note);
                deleteUnusedImages(note);

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(new ResponseValue<Void>());
                        }
                    }
                });
            }
        });

    }

    public void uploadNote(final Note note, final ControllerCallback<Void> cb) {
        if (note == null) {
            return;
        }
        List<NoteElement> elms = AliJsonHelper.parseArray(note.content, NoteElement.class);
        if (ListUtils.isEmpty(elms)) {
            return;
        }
        for (NoteElement e : elms) {
            if (!e.type.equals(NoteElement.TYPE_TEXT)) {
                UploadFileParams param = UploadFileParams.newInstance().addFile(new FileOptions().setFileKey("file").setFilePath(e.path));
                NetKit.fileRequest().uploadFile(param, new FileTransferListener() {
                    @Override
                    public void onResponse(FileResponse fileResponse) {

                    }

                    @Override
                    public void onError(MvcError mvcError) {

                    }

                    @Override
                    public void onProgress(double v) {

                    }
                });
            }
        }

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                netNoteSource.save(note);

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(new ResponseValue<Void>());
                        }
                    }
                });
            }
        });

    }


    private void deleteUnusedImages(Note note) {
        File[] images = new File(note.imagesDir).listFiles();
        if (!ArrayUtils.isEmpty(images)) {
            for (File img : images) {
                if (StringUtils.equals(img.getName(), note.thumbNail)) {
                    continue;
                }
                if (note.content == null || !note.content.contains(img.getName())) {
                    img.delete();
                }

            }
        }
    }


}
