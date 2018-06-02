package com.lch.menote.user.controller;


import com.lch.menote.user.ServerRequestCode;
import com.lch.menote.user.data.mem.MemUserRepo;
import com.lch.menote.user.data.net.NetUserRepo;
import com.lch.menote.user.data.sp.SpUserRepo;
import com.lch.menote.user.domain.LoginResponse;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.TaskExecutor;
import com.lch.netkit.common.tool.UiHandler;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class UserController {

    private NetUserRepo netUserRepo = new NetUserRepo();
    private MemUserRepo mMemUserRepo = new MemUserRepo();
    private SpUserRepo mSpUserRepo = new SpUserRepo();

    public void register(final String userName, final String userPwd, final ControllerCallback<User> cb) {
        final ResponseValue<User> ret = new ResponseValue<>();

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ResponseValue<LoginResponse> res = netUserRepo.save(userName, userPwd);
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

                if (res.data.getStatus() != ServerRequestCode.RESPCODE_SUCCESS || res.data.getData() == null) {
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

                if (res.data.getStatus() != ServerRequestCode.RESPCODE_SUCCESS || res.data.getData() == null) {
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
