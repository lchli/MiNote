package com.lch.menote.user.ui

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.route.NoteRouteApi;
import com.lch.menote.user.UserDI;
import com.lch.menote.user.controller.UserController;
import com.lch.menote.user.presenterx.LoginPresenter;
import com.lch.menote.user.presenterx.UserPresenter;
import com.lch.menote.user.route.RouteCall;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.widget.CommonTitleView;


/**
 * Created by lchli on 2016/8/10.
 */
public class RegisterFragment extends BaseFragment {

    UserController userController = new UserController();
    CommonTitleView common_title;
    View register_widget;
    UserPresenter userPrensenter = UserDI.provideUserPresenter();
    LoginPresenter mLoginPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginPresenter = new LoginPresenter(null, null, new LoginPresenter.View() {
            @Override
            public void renderLoading() {

            }

            @Override
            public void dismissLoading() {

            }

            @Override
            public void renderLoginSuccess() {
                NoteRouteApi m = RouteCall.getNoteModule();
                if (m != null) {
                    m.onUserLogin(null);
                }

                UserFragmentContainer p = (UserFragmentContainer) getParentFragment();
                p.toUserCenter(false);
            }

            @Override
            public void renderLoginFail(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        common_title.addRightText(getString(R.string.user_goto_login_text), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFragmentContainer userFragmentContainer = (UserFragmentContainer) getParentFragment();
                userFragmentContainer.toLogin(true);
            }
        });
        common_title.setCenterText("", null);
        common_title.addLeftIcon(R.drawable.arrow_left_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFragmentContainer userFragmentContainer = (UserFragmentContainer) getParentFragment();
                userFragmentContainer.toUserCenter(true);
            }
        });
        register_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPrensenter.register(null, null, null, new ControllerCallback<User>() {
                    @Override
                    public void onSuccess(@Nullable User user) {
                        userPrensenter.savaUserSession(user, new ControllerCallback<Void>() {
                            @Override
                            public void onSuccess(@Nullable Void aVoid) {
                                NoteRouteApi m = RouteCall.getNoteModule();
                                if (m != null) {
                                    m.onUserLogin(null);
                                }

                                UserFragmentContainer p = (UserFragmentContainer) getParentFragment();
                                p.toUserCenter(false);
                            }

                            @Override
                            public void onError(int i, String s) {
                                ToastUtils.showShort(s);
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        ToastUtils.showShort(s);

                    }
                });
            }
        });


    }


//    private fun registerAsync() {
//        val username = user_account_edit.text.toString()
//        val pwd = user_pwd_edit.text.toString()
//
//        userController.register(username, pwd, null,object : ControllerCallback<User> {
//
//            override fun onComplete(res: ResponseValue<User>) {
//                if (res.hasError() || res.data == null) {
//                    ToastUtils.showLong(res.errMsg())
//                    return
//                }
//
//                val mod = RouteCall.getNoteModule()
//                mod?.onUserLogin(null)
//
//                val userFragmentContainer = parentFragment as UserFragmentContainer
//                userFragmentContainer.toUserCenter(false)
//
//            }
//        })
//
//    }

}