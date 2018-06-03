package com.lch.menote.user.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.R;
import com.lch.menote.user.controller.UserController;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.base.BaseCompatActivity;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.VF;

/**
 * Created by lichenghang on 2018/6/3.
 */

public class UserInfoActivity extends BaseCompatActivity {

    private TextView user_nick;
    private TextView user_contact;
    private ImageView user_portrait;
    private UserController userController = new UserController();

    public static void launch(String userId, Context context) {

        Intent it = new Intent(context, UserInfoActivity.class);
        it.putExtra("userId", userId);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(it);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        user_nick = VF.f(this, R.id.user_nick);
        user_portrait = VF.f(this, R.id.user_portrait);
        user_contact = VF.f(this, R.id.user_contact);

        String userId = getIntent().getStringExtra("userId");

        if (TextUtils.isEmpty(userId)) {
            ToastUtils.showShort("用户id不存在");
            return;
        }

        userController.getById(userId, new ControllerCallback<User>() {
            @Override
            public void onComplete(@NonNull ResponseValue<User> responseValue) {
                if (responseValue.hasError() || responseValue.data == null) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }

                user_nick.setText(responseValue.data.name);

                if (TextUtils.isEmpty(responseValue.data.userContact)) {
                    user_contact.setText("联系方式:未填写");
                } else {
                    user_contact.setText("联系方式:" + responseValue.data.userContact);
                }

                Glide.with(UserInfoActivity.this).load(responseValue.data.headUrl).apply(RequestOptions
                        .placeholderOf(R.drawable.add_portrait)).into(user_portrait);

            }
        });

    }


}
