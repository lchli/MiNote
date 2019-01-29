package com.lch.menote.user.presenterx;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lch.menote.home.route.HomeRouteApi;
import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.domain.SaveLockPwdCase;
import com.lch.menote.user.route.RouteCall;
import com.lchli.arch.clean.ControllerCallback;

/**
 * presenter负责将model转换成视图需要的viewmodel
 */
public class LockPwdPresenter {

    public interface MvpView {
        void showLoading();

        void dismissLoading();

        void showFail(String msg);

        void finishUi();

    }

    private final MvpView view;
    private final SaveLockPwdCase saveLockPwdCase = new SaveLockPwdCase(UserModuleInjector.getINS().providePwdSource());

    public LockPwdPresenter(@NonNull MvpView view) {
        this.view = view;
    }

    public void onSavePwd(String pwd) {
        SaveLockPwdCase.Params p = new SaveLockPwdCase.Params();
        p.pwd = pwd;

        view.showLoading();
        saveLockPwdCase.invokeAsync(p, new ControllerCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void v) {
                view.dismissLoading();

                HomeRouteApi m = RouteCall.getHomeModule();
                if (m != null) {
                    m.launchHome(null);
                }

                view.finishUi();
            }

            @Override
            public void onError(int i, String s) {
                view.showFail(s);
                view.dismissLoading();
            }
        });
    }


}
