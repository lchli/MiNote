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
import com.lch.menote.note.ui.MyCloudNoteUi;
import com.lch.menote.user.presenterx.UserCenterPresenter;
import com.lch.menote.user.widget.UserCenterListItem;
import com.lch.menote.utils.DialogTool;
import com.lchli.utils.base.BaseFragment;
import com.lchli.utils.tool.ListUtils;
import com.lchli.utils.tool.Navigator;
import com.lchli.utils.tool.VF;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by lichenghang on 2018/6/3.
 */

public class UserFragmentUi extends BaseFragment implements UserCenterPresenter.MvpView {
    private static final int SELECT_IMG_RQUEST = 1;

    private TextView user_nick;
    private TextView user_contact;
    private UserCenterListItem logout_widget;
    private UserCenterListItem app_version_widget;
    private UserCenterListItem check_update_widget;
    private UserCenterListItem myCloudNoteView;
    private ImageView user_portrait;
    private UserCenterPresenter userCenterViewModel;
    private ProgressDialog mLoadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userCenterViewModel = new UserCenterPresenter(getActivity(), new ViewProxy(this));
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
        myCloudNoteView = VF.f(view, R.id.myCloudNoteView);

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
        myCloudNoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigator.launchActivity(getActivity(), MyCloudNoteUi.class);
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

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showLong(msg);
    }

    @Override
    public void toLogin() {
        UserFragmentContainer parent = (UserFragmentContainer) getParentFragment();
        if (parent != null) {
            parent.toLogin(true);
        }
    }

    @Override
    public void showHead(String headImgUrl) {
        Glide.with(getActivity()).load(headImgUrl).apply(RequestOptions
                .placeholderOf(R.drawable.add_portrait)).into(user_portrait);
    }

    @Override
    public void showHead(int resId) {
        user_portrait.setImageResource(resId);
    }

    @Override
    public void showContactText(String text) {
        user_contact.setText(text);
    }

    @Override
    public void showAppVersion(String text) {
        app_version_widget.setText(text);
    }

    @Override
    public void showNick(String text) {
        user_nick.setText(text);
    }

    @Override
    public void showAppUpdateResult(String text) {
        ToastUtils.showShort(text);
    }

    @Override
    public void showLogout(String text) {
        logout_widget.setText(text);
    }

    private static class ViewProxy implements UserCenterPresenter.MvpView {

        private final WeakReference<UserCenterPresenter.MvpView> uiRef;

        private ViewProxy(UserCenterPresenter.MvpView activity) {
            this.uiRef = new WeakReference<>(activity);
        }

        @Override
        public void showLoading() {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showLoading();
            }
        }

        @Override
        public void dismissLoading() {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.dismissLoading();
            }
        }

        @Override
        public void toLogin() {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.toLogin();
            }
        }

        @Override
        public void showHead(String headImgUrl) {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showHead(headImgUrl);
            }
        }

        @Override
        public void showHead(int resId) {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showHead(resId);
            }
        }

        @Override
        public void showContactText(String text) {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showContactText(text);
            }
        }

        @Override
        public void showAppVersion(String text) {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showAppVersion(text);
            }
        }

        @Override
        public void showNick(String text) {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showNick(text);
            }
        }

        @Override
        public void showAppUpdateResult(String text) {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showAppUpdateResult(text);
            }
        }

        @Override
        public void showLogout(String text) {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showLogout(text);
            }
        }

        @Override
        public void showFail(String msg) {
            final UserCenterPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showFail(msg);
            }
        }

    }

}
