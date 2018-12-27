package com.lch.menote.user.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.R;
import com.lch.menote.note.route.NoteRouteApi;
import com.lch.menote.user.route.RouteCall;
import com.lch.menote.user.viewmodel.UserCenterViewModel;
import com.lch.menote.user.widget.UserCenterListItem;
import com.lch.menote.utils.DialogTool;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.tool.ListUtils;
import com.lch.netkit.common.tool.VF;

import java.util.List;

import androidx.lifecycle.Observer;

/**
 * Created by lichenghang on 2018/6/3.
 */

public class UserFragmentUi extends BaseFragment {
    private static final int SELECT_IMG_RQUEST = 1;

    private TextView user_nick;
    private TextView user_contact;
    private UserCenterListItem logout_widget;
    private UserCenterListItem app_version_widget;
    private UserCenterListItem check_update_widget;
    private ImageView user_portrait;
    private UserCenterViewModel userCenterViewModel;
    private ProgressDialog mLoadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userCenterViewModel = new UserCenterViewModel(getActivity());
        mLoadingDialog = new ProgressDialog(getActivity());
    }

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

        userCenterViewModel.userNickTextVM.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                user_nick.setText(s);
            }
        });
        userCenterViewModel.logoutTextVM.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                logout_widget.setText(s);
            }
        });
        userCenterViewModel.userContactTextVM.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                user_contact.setText(s);
            }
        });
        userCenterViewModel.headImagePathVM.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(getActivity()).load(s).apply(RequestOptions
                        .placeholderOf(R.drawable.add_portrait)).into(user_portrait);
            }
        });
        userCenterViewModel.headImageResVM.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                user_portrait.setImageResource(integer);
            }
        });
        userCenterViewModel.appVersionTextVM.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                app_version_widget.setText(s);
            }
        });
        userCenterViewModel.appUpdateResult.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ToastUtils.showShort(s);
            }
        });

        check_update_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCenterViewModel.onCheckAppUpdate();
            }
        });

        user_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG); // Mode：Mode.SINGLE_IMG, Mode.MULTI_IMG, Mode.VIDEO
                config.needCamera(R.drawable.ic_camera).needGif(); // camera, gif support, set selected images count

                Boxing.of(config).withIntent(getActivity(), BoxingActivity.class).start(UserFragmentUi.this, SELECT_IMG_RQUEST);

            }
        });

        logout_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCenterViewModel.onLogoutOrInButtonClick();
            }
        });

        user_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModifyContactDialog();
            }
        });

        user_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCenterViewModel.onNickButtonClick();
            }
        });

        userCenterViewModel.failMsg.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ToastUtils.showLong(s);
            }
        });

        userCenterViewModel.loading.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mLoadingDialog.show();
                } else {
                    mLoadingDialog.dismiss();
                }

            }
        });

        userCenterViewModel.gotoLoginAction.observeForever(new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                UserFragmentContainer parent = (UserFragmentContainer) getParentFragment();
                if (parent != null) {
                    parent.toLogin(true);
                }

            }
        });

        userCenterViewModel.onLogoutAction.observeForever(new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                NoteRouteApi mod = RouteCall.getNoteModule();
                if (mod != null) {
                    mod.onUserLogout(null);
                }
            }
        });


        userCenterViewModel.onLoadUserInfo();
    }


    private void showModifyContactDialog() {

        DialogTool.showInputDialog(getActivity(), "修改联系方式", new DialogTool.InputDialogListener() {
            @Override
            public void onConfirm(final Dialog dialog, String inputText) {
                final String newtag = inputText;
                if (TextUtils.isEmpty(newtag)) {
                    return;
                }
                dialog.dismiss();

                userCenterViewModel.onModifyUserContact(newtag);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMG_RQUEST && resultCode == Activity.RESULT_OK) {
            final List<BaseMedia> medias = Boxing.getResult(data);
            if (!ListUtils.isEmpty(medias)) {
                userCenterViewModel.onModifyUserHead(medias.get(0).getPath());
            }
        }
    }


}
