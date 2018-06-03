package com.lch.menote.user.controller;


import android.text.TextUtils;

import com.lch.menote.ApiConstants;
import com.lch.menote.note.domain.UploadFileResponse;
import com.lch.menote.user.data.mem.MemUserRepo;
import com.lch.menote.user.data.net.NetUserRepo;
import com.lch.menote.user.data.sp.SpUserRepo;
import com.lch.menote.user.domain.LoginResponse;
import com.lch.menote.user.route.User;
import com.lch.netkit.NetKit;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.common.tool.TaskExecutor;
import com.lch.netkit.common.tool.UiHandler;
import com.lch.netkit.file.helper.FileOptions;
import com.lch.netkit.file.helper.UploadFileParams;
import com.lch.netkit.string.Parser;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class UserController {

    private NetUserRepo netUserRepo = new NetUserRepo();
    private MemUserRepo mMemUserRepo = new MemUserRepo();
    private SpUserRepo mSpUserRepo = new SpUserRepo();

    public void register(final String userName, final String userPwd, final String userHeadUrl,
                         final ControllerCallback<User> cb) {
        final ResponseValue<User> ret = new ResponseValue<>();

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ResponseValue<LoginResponse> res = netUserRepo.save(userName, userPwd, userHeadUrl);
                if (res.hasError() || res.data == null) {
                    ret.setErrMsg(res.errMsg());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onComplete(ret);
                        }
                    });
                    return;
                }

                if (res.data.getStatus() != ApiConstants.RESPCODE_SUCCESS || res.data.getData() == null) {
                    ret.setErrMsg(res.data.getMessage());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onComplete(ret);
                        }
                    });
                    return;
                }

                mSpUserRepo.saveUser(res.data.getData());

                ret.data = res.data.getData();


                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(ret);
                    }
                });

            }
        });


    }

    public void update(final User user,final String userHead, final ControllerCallback<User> cb) {
        final ResponseValue<User> ret = new ResponseValue<>();

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if (!TextUtils.isEmpty(userHead)) {

                    UploadFileParams param = UploadFileParams.newInstance()
                            .setUrl(ApiConstants.UPLOAD_FILE)
                            .addFile(new FileOptions().setFileKey("file").setFilePath(userHead));

                    ResponseValue<UploadFileResponse> resf = NetKit.fileRequest().uploadFileSync(param, new Parser<UploadFileResponse>() {
                        @Override
                        public UploadFileResponse parse(String s) {
                            return AliJsonHelper.parseObject(s, UploadFileResponse.class);
                        }
                    });

                    if (resf.hasError()) {
                        ret.setErrMsg(resf.errMsg());
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

                    if (resf.data == null) {
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

                    if (resf.data.status != ApiConstants.RESPCODE_SUCCESS) {
                        ret.setErrMsg(resf.data.message);
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

                    user.headUrl = resf.data.data;

                }


                ResponseValue<LoginResponse> res = netUserRepo.update(user);
                if (res.hasError() || res.data == null) {
                    ret.setErrMsg(res.errMsg());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onComplete(ret);
                        }
                    });
                    return;
                }

                if (res.data.getStatus() != ApiConstants.RESPCODE_SUCCESS || res.data.getData() == null) {
                    ret.setErrMsg(res.data.getMessage());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onComplete(ret);
                        }
                    });
                    return;
                }

                mSpUserRepo.saveUser(res.data.getData());

                ret.data = res.data.getData();


                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(ret);
                    }
                });

            }
        });


    }

    public void login(final String userName, final String userPwd, final ControllerCallback<User> cb) {
        final ResponseValue<User> ret = new ResponseValue<>();

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ResponseValue<LoginResponse> res = netUserRepo.get(userName, userPwd);
                if (res.hasError() || res.data == null) {
                    ret.setErrMsg(res.errMsg());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onComplete(ret);
                        }
                    });
                    return;
                }

                if (res.data.getStatus() != ApiConstants.RESPCODE_SUCCESS || res.data.getData() == null) {
                    ret.setErrMsg(res.data.getMessage());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onComplete(ret);
                        }
                    });
                    return;
                }

                mSpUserRepo.saveUser(res.data.getData());

                ret.data = res.data.getData();


                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(ret);
                    }
                });

            }
        });
    }

    public void getById(final String userId, final ControllerCallback<User> cb) {
        final ResponseValue<User> ret = new ResponseValue<>();

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ResponseValue<LoginResponse> res = netUserRepo.getById(userId);
                if (res.hasError() || res.data == null) {
                    ret.setErrMsg(res.errMsg());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onComplete(ret);
                        }
                    });
                    return;
                }

                if (res.data.getStatus() != ApiConstants.RESPCODE_SUCCESS || res.data.getData() == null) {
                    ret.setErrMsg(res.data.getMessage());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onComplete(ret);
                        }
                    });
                    return;
                }

                ret.data = res.data.getData();


                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(ret);
                    }
                });

            }
        });
    }

    public void saveLockPwd(final String pwd, final ControllerCallback<Void> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<Void> res = mMemUserRepo.saveLockPwd(pwd);

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(res);
                    }
                });
            }
        });
    }

    public void getLockPwd(final ControllerCallback<String> cb) {

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<String> res = mMemUserRepo.getLockPwd();

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(res);
                    }
                });
            }
        });
    }

    public void getUserSession(final ControllerCallback<User> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<User> res = mSpUserRepo.getUser();

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(res);
                    }
                });
            }
        });
    }

    public void saveUserSession(final User user, final ControllerCallback<Void> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<Void> res = mSpUserRepo.saveUser(user);

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(res);
                    }
                });
            }
        });
    }

    public void clearUserSession(final ControllerCallback<Void> cb) {
        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<Void> res = mSpUserRepo.removeCurrentUser();

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(res);
                    }
                });
            }
        });
    }
}
