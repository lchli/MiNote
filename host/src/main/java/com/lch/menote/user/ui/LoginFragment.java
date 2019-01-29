package com.lch.menote.user.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.user.presenterx.LoginPresenter;
import com.lchli.utils.base.BaseFragment;
import com.lchli.utils.tool.VF;
import com.lchli.utils.widget.CommonTitleView;

import java.lang.ref.WeakReference;


/**
 * Created by lchli on 2016/8/10.
 */
public class LoginFragment extends BaseFragment implements LoginPresenter.MvpView {

    private CommonTitleView common_title;
    private View login_widget;
    private LoginPresenter mLoginViewModel;
    private ProgressDialog mLoadingDialog;
    private EditText user_account_edit;
    private EditText user_pwd_edit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = new ProgressDialog(getActivity());
        mLoginViewModel = new LoginPresenter(new ViewProxy(this));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        common_title = VF.f(view, R.id.common_title);
        login_widget = VF.f(view, R.id.login_widget);
        user_account_edit = VF.f(view, R.id.user_account_edit);
        user_pwd_edit = VF.f(view, R.id.user_pwd_edit);

        common_title.addRightText(getString(R.string.user_goto_register_text), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFragmentContainer container = (UserFragmentContainer) getParentFragment();
                container.toRegister();
            }
        });
        common_title.setCenterText("", null);
        common_title.addLeftIcon(R.drawable.arrow_left_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFragmentContainer container = (UserFragmentContainer) getParentFragment();
                container.toUserCenter(true);
            }
        });

        login_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginViewModel.login(user_account_edit.getText().toString(), user_pwd_edit.getText().toString());
            }
        });
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showLong(msg);
    }

    @Override
    public void toUserCenter() {
        UserFragmentContainer p = (UserFragmentContainer) getParentFragment();
        p.toUserCenter(false);
    }

    @Override
    public void dismissLoading() {
        mLoadingDialog.dismiss();
    }


    private static class ViewProxy implements LoginPresenter.MvpView {

        private final WeakReference<LoginPresenter.MvpView> uiRef;

        private ViewProxy(LoginPresenter.MvpView activity) {
            this.uiRef = new WeakReference<>(activity);
        }

        @Override
        public void showLoading() {
            final LoginPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showLoading();
            }
        }

        @Override
        public void dismissLoading() {
            final LoginPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.dismissLoading();
            }
        }

        @Override
        public void toUserCenter() {
            final LoginPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.toUserCenter();
            }
        }

        @Override
        public void showFail(String msg) {
            final LoginPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showFail(msg);
            }
        }

    }
}