package com.lch.menote.user.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.ApiConstants;
import com.lch.menote.R;
import com.lch.menote.app.ApkController;
import com.lch.menote.app.model.ApkResponse;
import com.lch.menote.note.route.NoteRouteApi;
import com.lch.menote.user.controller.UserController;
import com.lch.menote.user.route.RouteCall;
import com.lch.menote.user.route.User;
import com.lch.menote.utils.DialogTool;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.ListUtils;
import com.lch.netkit.common.tool.VF;

import java.util.List;

/**
 * Created by lichenghang on 2018/6/3.
 */

public class UserFragmentUi extends BaseFragment {
    private static final int SELECT_IMG_RQUEST = 1;

    private UserController mUserController = new UserController();
    private TextView user_nick;
    private TextView user_contact;
    private UserCenterListItem logout_widget;
    private UserCenterListItem app_version_widget;
    private UserCenterListItem check_update_widget;
    private ImageView user_portrait;
    private ApkController apkController = new ApkController();

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
        user_portrait = VF.f(view, R.id.user_portrait);
        user_contact = VF.f(view, R.id.user_contact);
        check_update_widget = VF.f(view, R.id.check_update_widget);

        check_update_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkController.checkUpdate(AppUtils.getAppVersionCode(), new ControllerCallback<ApkResponse>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<ApkResponse> responseValue) {
                        if (responseValue.hasError()||responseValue.data==null||responseValue.data.status!= ApiConstants.RESPCODE_SUCCESS) {
                            ToastUtils.showShort(responseValue.errMsg());
                            return;
                        }
                        ToastUtils.showShort("已有新版本，请到应用宝市场下载！");

                    }
                });
            }
        });

        user_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_portrait.setEnabled(false);

                mUserController.getUserSession(new ControllerCallback<User>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<User> responseValue) {
                        user_portrait.setEnabled(true);

                        if (responseValue.data == null) {
                            return;
                        }

                        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG); // Mode：Mode.SINGLE_IMG, Mode.MULTI_IMG, Mode.VIDEO
                        config.needCamera(R.drawable.ic_camera).needGif(); // camera, gif support, set selected images count

                        Boxing.of(config).withIntent(getActivity(), BoxingActivity.class).start(UserFragmentUi.this, SELECT_IMG_RQUEST);

                    }
                });

            }
        });

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

        user_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTagDialog();
            }
        });


        refreshUi();
    }


    private void showAddTagDialog() {

        DialogTool.showInputDialog(getActivity(), "修改联系方式", new DialogTool.InputDialogListener() {
            @Override
            public void onConfirm(final Dialog dialog, String inputText) {
                final String newtag = inputText;

                if (TextUtils.isEmpty(newtag)) {
                    return;
                }

                mUserController.getUserSession(new ControllerCallback<User>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<User> responseValue) {

                        responseValue.data.userContact = newtag;

                        mUserController.update(responseValue.data, null, new ControllerCallback<User>() {
                            @Override
                            public void onComplete(@NonNull ResponseValue<User> responseValue) {
                                if (responseValue.hasError()) {
                                    ToastUtils.showShort(responseValue.errMsg());
                                } else {
                                    dialog.dismiss();
                                    ToastUtils.showShort("添加成功");

                                    refreshUi();
                                }

                            }
                        });
                    }
                });
            }
        });


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
                    user_contact.setText("");
                    user_portrait.setImageResource(R.drawable.add_portrait);

                } else {
                    user_nick.setText(responseValue.data.name);

                    if (TextUtils.isEmpty(responseValue.data.userContact)) {
                        user_contact.setText("联系方式:未填写");
                    } else {
                        user_contact.setText("联系方式:" + responseValue.data.userContact);
                    }

                    Glide.with(getActivity()).load(responseValue.data.headUrl).apply(RequestOptions
                            .placeholderOf(R.drawable.add_portrait)).into(user_portrait);

                    logout_widget.setText(getString(R.string.logout));
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMG_RQUEST && resultCode == Activity.RESULT_OK) {
            final List<BaseMedia> medias = Boxing.getResult(data);
            if (!ListUtils.isEmpty(medias)) {

                mUserController.getUserSession(new ControllerCallback<User>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<User> responseValue) {
                        if (responseValue.data == null) {
                            return;
                        }

                        mUserController.update(responseValue.data, medias.get(0).getPath(), new ControllerCallback<User>() {
                            @Override
                            public void onComplete(@NonNull ResponseValue<User> responseValue) {
                                if (responseValue.hasError()) {
                                    ToastUtils.showShort("err:" + responseValue.errMsg());
                                    return;
                                }

                                refreshUi();

                            }
                        });
                    }
                });
            }
        }
    }
}
