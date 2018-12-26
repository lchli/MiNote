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
import com.lch.menote.user.route.RouteCall;
import com.lch.menote.user.viewmodel.LoginViewModel;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.tool.VF;
import com.lch.netkit.common.widget.CommonTitleView;

import androidx.lifecycle.Observer;


/**
 * Created by lchli on 2016/8/10.
 */
public class LoginFragment extends BaseFragment {

    private CommonTitleView common_title;
    private View login_widget;
    private final LoginViewModel mLoginViewModel = new LoginViewModel();
    private ProgressDialog mLoadingDialog;
    private EditText user_account_edit;
    private EditText user_pwd_edit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = new ProgressDialog(getActivity());
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
                mLoginViewModel.onLogin(user_account_edit.getText().toString(), user_pwd_edit.getText().toString());
            }
        });


        mLoginViewModel.loading.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mLoadingDialog.show();
                } else {
                    mLoadingDialog.dismiss();
                }
            }
        });
        mLoginViewModel.failMsg.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ToastUtils.showLong(s);
            }
        });
        mLoginViewModel.successEvent.observeForever(new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                RouteCall.getNoteModule().onUserLogin(null);

                UserFragmentContainer container = (UserFragmentContainer) getParentFragment();
                container.toUserCenter(false);
            }
        });

    }


}