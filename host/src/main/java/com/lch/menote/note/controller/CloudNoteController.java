package com.lch.menote.note.controller;

import android.content.Context;
import android.text.TextUtils;

import com.lch.menote.ApiConstants;
import com.lch.menote.R;
import com.lch.menote.note.data.net.NetNoteRepo;
import com.lch.menote.note.domain.NoteElement;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.note.domain.response.QueryNoteResponse;
import com.lch.menote.note.domain.response.UploadFileResponse;
import com.lch.menote.note.route.RouteCall;
import com.lch.menote.user.data.sp.SpUserRepo;
import com.lch.menote.user.route.User;
import com.lch.menote.user.route.UserRouteApi;
import com.lch.menote.utils.RequestUtils;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.common.tool.ContextProvider;
import com.lch.netkit.common.tool.ListUtils;
import com.lch.netkit.common.tool.TaskExecutor;
import com.lch.netkit.common.tool.UiHandler;
import com.lch.netkit.file.helper.FileOptions;
import com.lch.netkit.file.helper.UploadFileParams;
import com.lch.netkit.string.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class CloudNoteController {


    private final Context context;
    private NetNoteRepo netNoteSource;
    private int page;
    private static final int PAGE_SIZE = 20;
    private List<NoteModel> all = new ArrayList<>();
    private boolean isHaveMore = false;
    private NetNoteRepo.NetNoteQuery mQuery;

    public CloudNoteController(Context context) {
        netNoteSource = new NetNoteRepo();
        this.context = context;
    }


    private void getCloudNotes(final ControllerCallback<List<NoteModel>> cb) {
        final ResponseValue<List<NoteModel>> ret = new ResponseValue<>();

        String userId = null;

        UserRouteApi m = RouteCall.getUserModule();
        if (m != null && m.userSession() != null) {
            userId = m.userSession().uid;
        }

        if (TextUtils.isEmpty(userId)) {
            ret.setErrMsg(context.getString(R.string.not_login));
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

        mQuery.setPage(page).setPageSize(PAGE_SIZE).setUseId(userId);

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                final ResponseValue<QueryNoteResponse> r = netNoteSource.queryNotes(mQuery);

                if (r.hasError() || r.data == null) {
                    ret.setErrMsg(r.errMsg());
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

                List<NoteModel> notes = r.data.data;
                if (!ListUtils.isEmpty(notes)) {
                    all.addAll(notes);
                }

                if (ListUtils.isEmpty(notes) || notes.size() < PAGE_SIZE) {
                    isHaveMore = false;
                }

                List<NoteModel> result = new ArrayList<>();
                result.addAll(all);

                ret.data = result;

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

    public void refresh(final NetNoteRepo.NetNoteQuery query, final ControllerCallback<List<NoteModel>> cb) {
        isHaveMore = true;
        all.clear();
        page = 0;
        mQuery = query;

        getCloudNotes(cb);

    }

    public void loadMore(final ControllerCallback<List<NoteModel>> cb) {
        final ResponseValue<List<NoteModel>> ret = new ResponseValue<>();

        if (!isHaveMore) {
            ret.setErrMsg("已无更多数据");
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

        getCloudNotes(cb);

    }


    public void updateNetNote(final NoteModel note, final ControllerCallback<Void> cb) {
        saveNoteToNetImpl(note, cb);
    }

    public void publicNetNote(final NoteModel note, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
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

    public void likeNetNote(final String noteId, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<Void> resSave = netNoteSource.likeNote(noteId);

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

    public void getNoteById(final String noteId, final ControllerCallback<NoteModel> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<NoteModel> ret = new ResponseValue<>();

                NetNoteRepo.NetNoteQuery query = new NetNoteRepo.NetNoteQuery();
                query.setUid(noteId);

                ResponseValue<QueryNoteResponse> res = netNoteSource.queryNotes(query);

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

                if (res.data == null || ListUtils.isEmpty(res.data.data)) {
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

                ret.data = res.data.data.get(0);

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


    public void deleteNetNote(final String noteId, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<Void> resSave = netNoteSource.delete(noteId);

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
        final User se = SpUserRepo.getINS().getUser().data;

        if (se == null || TextUtils.isEmpty(se.uid) || TextUtils.isEmpty(se.token)) {
            ret.setErrMsg(ContextProvider.context().getString(R.string.not_login));
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
                        UploadFileParams param = RequestUtils.minoteUploadFileParams()
                                .addParam("userId", se.uid)
                                .addParam("userToken", se.token)
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

                        if (res.data.status != ApiConstants.RESPONSE_CODE_SUCCESS) {
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


}
