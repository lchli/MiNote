package com.lch.menote.user.controller;

import com.lch.menote.common.util.TaskExecutor;
import com.lch.menote.common.util.UiHandler;
import com.lch.menote.user.ServerRequestCode;
import com.lch.menote.user.data.DI;
import com.lch.menote.user.data.net.NetUserRepo;
import com.lch.menote.user.domain.LoginResponse;
import com.lch.menote.userapi.User;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class UserController {

    private NetUserRepo netUserRepo = new NetUserRepo();

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

                DI.provideSpSource().addUser(res.data.getData());

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

                DI.provideSpSource().addUser(res.data.getData());

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
}
