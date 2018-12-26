package com.lch.menote.user.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.route.NoteRouteApi;
import com.lch.menote.user.route.RouteCall;
import com.lch.menote.user.viewmodel.RegisterViewModel;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.tool.VF;
import com.lch.netkit.common.widget.CommonTitleView;

import androidx.lifecycle.Observer;


/**
 * Created by lchli on 2016/8/10.
 */
public class RegisterFragment extends BaseFragment {

    private CommonTitleView common_title;
    private View register_widget;

    private final RegisterViewModel mRegisterViewModel = new RegisterViewModel();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_account_edit = VF.f(view, R.id.user_account_edit);
        user_pwd_edit = VF.f(view, R.id.user_pwd_edit);
        common_title = VF.f(view, R.id.common_title);
        register_widget = VF.f(view, R.id.register_widget);

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
                mRegisterViewModel.onRegister(user_account_edit.getText().toString(), user_pwd_edit.getText().toString(), null);
            }
        });

        mRegisterViewModel.loading.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mLoadingDialog.show();
                } else {
                    mLoadingDialog.dismiss();
                }
            }
        });
        mRegisterViewModel.failMsg.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ToastUtils.showLong(s);
            }
        });
        mRegisterViewModel.successEvent.observeForever(new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                NoteRouteApi m = RouteCall.getNoteModule();
                if (m != null) {
                    m.onUserLogin(null);
                }

                UserFragmentContainer p = (UserFragmentContainer) getParentFragment();
                p.toUserCenter(false);
            }
        });

    }


}