package com.lch.menote.user.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.route.NoteRouteApi;
import com.lch.menote.user.controller.UserController;
import com.lch.menote.user.route.RouteCall;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.VF;

/**
 * Created by lichenghang on 2018/6/3.
 */

public class UserFragmentUi extends BaseFragment {
    private UserController mUserController = new UserController();
    private TextView user_nick;
    private UserCenterListItem logout_widget;
    private UserCenterListItem app_version_widget;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logout_widget = VF.f(view, R.id.logout_widget);
        app_version_widget = VF.f(view, R.id.app_version_widget);
        user_nick = VF.f(view, R.id.user_nick);

        app_version_widget.setText(getString(R.string.app_version_pattern, AppUtils.getAppVersionName()));

        logout_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_widget.setEnabled(false);

                mUserController.getUserSession(new ControllerCallback<User>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<User> responseValue) {
                        logout_widget.setEnabled(true);

                        if (responseValue.data == null) {
                            UserFragmentContainer parent = (UserFragmentContainer) getParentFragment();
                            if (parent != null) {
                                parent.toLogin(true);
                            }

                        } else {

                            mUserController.clearUserSession(new ControllerCallback<Void>() {
                                @Override
                                public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                                    NoteRouteApi mod = RouteCall.getNoteModule();
                                    if (mod != null) {
                                        mod.onUserLogout(null);
                                    }

                                    refreshUi();
                                }
                            });

                        }

                    }
                });


            }
        });

        refreshUi();
    }


    private void refreshUi() {
        mUserController.getUserSession(new ControllerCallback<User>() {
            @Override
            public void onComplete(@NonNull ResponseValue<User> responseValue) {
                if (responseValue.hasError()) {
                    ToastUtils.showLong(responseValue.errMsg());
                } else if (responseValue.data == null) {//not login ui.
                    user_nick.setText(getString(R.string.not_login));
                    logout_widget.setText(getString(R.string.click_to_login));

                } else {
                    user_nick.setText(responseValue.data.name);
                    logout_widget.setText(getString(R.string.logout));
                }
            }
        });
    }
}
