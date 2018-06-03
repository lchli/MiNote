package com.lch.menote.note.controller;

import android.content.Context;

import com.lch.menote.ApiConstants;
import com.lch.menote.note.data.NoteSource;
import com.lch.menote.note.data.db.DatabseNoteRepo;
import com.lch.menote.note.data.net.NetNoteRepo;
import com.lch.menote.note.domain.HeadData;
import com.lch.menote.note.helper.ModelMapper;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteElement;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.note.domain.NotePinedData;
import com.lch.menote.note.domain.QueryNoteResponse;
import com.lch.menote.note.domain.UploadFileResponse;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.common.tool.ListUtils;
import com.lch.netkit.common.tool.TaskExecutor;
import com.lch.netkit.common.tool.UiHandler;
import com.lch.netkit.file.helper.FileOptions;
import com.lch.netkit.file.helper.UploadFileParams;
import com.lch.netkit.string.Parser;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class NoteController {


    private DatabseNoteRepo localNoteSource;
    private NetNoteRepo netNoteSource;

    public NoteController(Context context) {
        localNoteSource = new DatabseNoteRepo(context);
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

        ResponseValue<QueryNoteResponse> notesRes = source.queryNotes(tag, title, sortTimeAsc, useId);
        if (notesRes.hasError() || notesRes.data == null) {
            res.err = notesRes.err;
            return res;
        }

        List<NoteModel> notes = notesRes.data.data;
        if (notes == null || notes.isEmpty()) {
            return res;
        }

        List<Object> all = new ArrayList<>();
        res.data = all;

        all.add(new HeadData());


        String preType = "";

        for (NoteModel note : notes) {
            String currentType = note.type;
            if (!preType.equals(currentType)) {
                all.add(new NotePinedData(-1, currentType));
                preType = currentType;
            }

            all.add(note);
        }

        return res;
    }

    public void getLocalNotes(final String tag, final String title, final boolean sortTimeAsc, final String useId, final ControllerCallback<List<NoteModel>> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<QueryNoteResponse> r = localNoteSource.queryNotes(tag, title, sortTimeAsc, useId);
                final ResponseValue<List<NoteModel>> ret = new ResponseValue<>();
                if (r.hasError() || r.data == null) {
                    ret.err = r.err;
                } else {
                    ret.data = r.data.data;
                }

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(ret);
                        }
                    }
                });

            }
        });

    }

    public void getCloudNotes(final String tag, final String title, final boolean sortTimeAsc, final String useId, final ControllerCallback<List<NoteModel>> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<QueryNoteResponse> r = netNoteSource.queryNotes(tag, title, sortTimeAsc, useId);

                final ResponseValue<List<NoteModel>> ret = new ResponseValue<>();
                if (r.hasError() || r.data == null) {
                    ret.err = r.err;
                } else {
                    ret.data = r.data.data;
                }


                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(ret);
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


    public void saveLocalNote(final NoteModel note, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                localNoteSource.save(note);
                //deleteUnusedImages(note);

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

    public void deleteLocalNote(final NoteModel note, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                localNoteSource.delete(ModelMapper.from(note));

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

    public void updateNetNote(final NoteModel note, final ControllerCallback<Void> cb) {
        saveNoteToNetImpl(note, cb);
    }

    public void saveNoteToNet(final NoteModel note, final ControllerCallback<Void> cb) {
        note.uid = null;//server will generate uid.
        saveNoteToNetImpl(note, cb);
    }


    private void saveNoteToNetImpl(final NoteModel note, final ControllerCallback<Void> cb) {

        final ResponseValue<Void> ret = new ResponseValue<>();

        if (note == null) {
            ret.setErrMsg("note is null.");
            UiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (cb != null) {
                        cb.onComplete(ret);
                    }
                }
            });
            return;
        }

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<NoteElement> elms = AliJsonHelper.parseArray(note.content, NoteElement.class);
                if (ListUtils.isEmpty(elms)) {
                    ret.setErrMsg("note content is empty.");
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (cb != null) {
                                cb.onComplete(ret);
                            }
                        }
                    });
                    return;
                }


                boolean isHasFile = false;

                for (NoteElement e : elms) {
                    if (!e.type.equals(NoteElement.TYPE_TEXT)) {
                        UploadFileParams param = UploadFileParams.newInstance()
                                .setUrl(ApiConstants.UPLOAD_FILE)
                                .addFile(new FileOptions().setFileKey("file").setFilePath(e.path));
                        ResponseValue<UploadFileResponse> res = NetKit.fileRequest().uploadFileSync(param, new Parser<UploadFileResponse>() {
                            @Override
                            public UploadFileResponse parse(String s) {
                                return AliJsonHelper.parseObject(s, UploadFileResponse.class);
                            }
                        });

                        if (res.hasError()) {
                            ret.setErrMsg(res.errMsg());
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (cb != null) {
                                        cb.onComplete(ret);
                                    }
                                }
                            });
                            return;
                        }

                        if (res.data == null) {
                            ret.setErrMsg("res data is null");
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (cb != null) {
                                        cb.onComplete(ret);
                                    }
                                }
                            });
                            return;
                        }

                        if (res.data.status != ApiConstants.RESPCODE_SUCCESS) {
                            ret.setErrMsg(res.data.message);
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (cb != null) {
                                        cb.onComplete(ret);
                                    }
                                }
                            });
                            return;
                        }

                        e.path = res.data.data;
                        isHasFile = true;
                    }
                }//for

                if (isHasFile) {
                    note.content = AliJsonHelper.toJSONString(elms);
                }

                final ResponseValue<Void> resSave = netNoteSource.save(note);

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(resSave);
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
