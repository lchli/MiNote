package com.lch.menote.user.presenterx;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.domain.GetUserInfoCase;
import com.lch.menote.user.domain.GetUserSessionCase;
import com.lch.menote.user.route.User;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * presenter负责将model转换成视图需要的viewmodel
 */
public class UserInfoPresenter {

    public interface MvpView {
        void showLoading();

        void dismissLoading();

        void showFail(String msg);

        void showHead(String headImgUrl);

        void showContactText(String text);

        void showNick(String text);

    }

    private final MvpView view;
    private final GetUserInfoCase getUserInfoCase = new GetUserInfoCase(UserModuleInjector.getINS().provideRemoteUserDataSource());
    private final GetUserSessionCase getUserSessionCase = new GetUserSessionCase(UserModuleInjector.getINS().provideLocalUserDataSource());

    public UserInfoPresenter(@NonNull MvpView view) {
        this.view = view;
    }

    public void getUserInfo(final String userId) {

        view.showLoading();
        UseCase.executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                final ResponseValue<User> res = getUserSessionCase.invokeSync(null);
                if (res.hasError()) {
                    view.dismissLoading();
                    view.showFail(res.getErrorMsg());
                    return;
                }

                if (res.data == null) {
                    view.dismissLoading();
                    view.showFail("未登陆");
                    return;
                }
                GetUserInfoCase.Params p = new GetUserInfoCase.Params();
                p.userId = userId;
                p.sessionUid = res.data.uid;
                p.sessionToken = res.data.token;

                ResponseValue<User> ret = getUserInfoCase.invokeSync(p);
                view.dismissLoading();

                if (ret.hasError()) {
                    view.showFail(ret.getErrorMsg());
                    return;
                }

                if (ret.data == null) {
                    view.showFail("用户id不存在");
                    return;
                }

                view.showNick(ret.data.name);
                if (TextUtils.isEmpty(ret.data.userContact)) {
                    view.showContactText("联系方式:未填写");
                } else {
                    view.showContactText("联系方式:" + ret.data.userContact);
                }
                view.showHead(ret.data.headUrl);

            }
        });
    }


}
