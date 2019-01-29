package com.lch.menote.user.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.R;
import com.lch.menote.user.presenterx.UserInfoPresenter;
import com.lchli.utils.base.BaseCompatActivity;
import com.lchli.utils.tool.VF;

import java.lang.ref.WeakReference;


/**
 * Created by lichenghang on 2018/6/3.
 */

public class UserInfoActivity extends BaseCompatActivity implements UserInfoPresenter.MvpView {

    private TextView user_nick;
    private TextView user_contact;
    private ImageView user_portrait;
    private UserInfoPresenter userInfoPresenter;

    public static void launch(String userId, Context context) {

        Intent it = new Intent(context, UserInfoActivity.class);
        it.putExtra("userId", userId);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(it);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        userInfoPresenter = new UserInfoPresenter(new ViewProxy(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        user_nick = VF.f(this, R.id.user_nick);
        user_portrait = VF.f(this, R.id.user_portrait);
        user_contact = VF.f(this, R.id.user_contact);

        String userId = getIntent().getStringExtra("userId");

        userInfoPresenter.getUserInfo(userId);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showHead(String headImgUrl) {
        Glide.with(UserInfoActivity.this).load(headImgUrl).apply(RequestOptions
                .placeholderOf(R.drawable.add_portrait)).into(user_portrait);
    }

    @Override
    public void showContactText(String text) {
        user_contact.setText(text);
    }

    @Override
    public void showNick(String text) {
        user_nick.setText(text);
    }


    private static class ViewProxy implements UserInfoPresenter.MvpView {

        private final WeakReference<UserInfoPresenter.MvpView> uiRef;

        private ViewProxy(UserInfoPresenter.MvpView activity) {
            this.uiRef = new WeakReference<>(activity);
        }

        @Override
        public void showLoading() {
            final UserInfoPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showLoading();
            }
        }

        @Override
        public void dismissLoading() {
            final UserInfoPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.dismissLoading();
            }
        }

        @Override
        public void showHead(String headImgUrl) {
            final UserInfoPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showHead(headImgUrl);
            }
        }

        @Override
        public void showContactText(String text) {
            final UserInfoPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showContactText(text);
            }
        }

        @Override
        public void showNick(String text) {
            final UserInfoPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showNick(text);
            }
        }

        @Override
        public void showFail(String msg) {
            final UserInfoPresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showFail(msg);
            }
        }

    }
}
