package com.lch.menote.user.presenterx;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.lch.menote.R;
import com.lch.menote.file.FileModuleInjector;
import com.lch.menote.note.NoteApiManager;
import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.domain.CheckAppUpdateCase;
import com.lch.menote.user.domain.ClearUserSessionCase;
import com.lch.menote.user.domain.GetUserSessionCase;
import com.lch.menote.user.domain.UpdateUserContactCase;
import com.lch.menote.user.domain.UpdateUserHeadIconCase;
import com.lch.menote.user.route.User;
import com.lch.menote.utils.MvpViewUtils;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;

/**
 * vm只负责更新ui状态模型即负责ui更新逻辑。
 * Created by Administrator on 2018/12/26.
 */

public class UserCenterPresenter {

    public interface MvpView {
        void showLoading();

        void dismissLoading();

        void showFail(String msg);

        void toLogin();

        void showHead(String headImgUrl);

        void showHead(int resId);

        void showContactText(String text);

        void showAppVersion(String text);

        void showNick(String text);

        void showAppUpdateResult(String text);

        void showLogout(String text);

    }

    private MvpView view;
    private Context context;

    private final UpdateUserHeadIconCase updateUserHeadIconCase = new UpdateUserHeadIconCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource(), FileModuleInjector.getINS().provideRemoteFileSource());

    private final UpdateUserContactCase updateUserContactCase = new UpdateUserContactCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource());

    private final GetUserSessionCase getUserSessionCase = new GetUserSessionCase(UserModuleInjector.getINS().provideLocalUserDataSource());
    private final ClearUserSessionCase clearUserSessionCase = new ClearUserSessionCase(UserModuleInjector.getINS().provideLocalUserDataSource());
    private final CheckAppUpdateCase checkAppUpdateCase = new CheckAppUpdateCase(UserModuleInjector.getINS().provideAppUpdateInfoDataSource());

    public UserCenterPresenter(Context context, MvpView view) {
        this.context = context;
        this.view = MvpViewUtils.newUiThreadProxy(view);
    }

    public void onModifyUserHead(String headPath) {
        UpdateUserHeadIconCase.UpdateParams params = new UpdateUserHeadIconCase.UpdateParams();
        params.headPath = headPath;

        view.showLoading();
        updateUserHeadIconCase.invokeAsync(params, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                view.dismissLoading();

                if (user == null) {
                    view.showFail("user is null.");
                    return;
                }

                view.showHead(user.headUrl);
            }

            @Override
            public void onError(int i, String s) {
                view.dismissLoading();
                view.showFail(s);
            }
        });
    }


    public void onModifyUserContact(String contact) {
        UpdateUserContactCase.UpdateParams params = new UpdateUserContactCase.UpdateParams();
        params.userContact = contact;

        view.showLoading();
        updateUserContactCase.invokeAsync(params, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                view.dismissLoading();

                if (user == null) {
                    view.showFail("user is null.");
                    return;
                }

                view.showContactText(user.userContact);
            }

            @Override
            public void onError(int i, String s) {
                view.dismissLoading();
                view.showFail(s);
            }
        });
    }


    public void onLoadUserInfo() {
        view.showAppVersion(context.getString(R.string.app_version_pattern, AppUtils.getAppVersionName()));

        view.showLoading();
        getUserSessionCase.invokeAsync(null, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                view.dismissLoading();

                if (user == null) {
                    view.showNick(context.getString(R.string.not_login));
                    view.showLogout(context.getString(R.string.click_to_login));
                    view.showContactText("");
                    view.showHead(R.drawable.add_portrait);

                } else {
                    view.showNick(user.name);
                    view.showLogout(context.getString(R.string.logout));
                    if (TextUtils.isEmpty(user.userContact)) {
                        view.showContactText("联系方式:未填写");
                    } else {
                        view.showContactText("联系方式:" + user.userContact);
                    }
                    view.showHead(user.headUrl);
                }
            }

            @Override
            public void onError(int i, String s) {
                view.dismissLoading();
                view.showFail(s);
            }
        });
    }

    public void onNickButtonClick() {
        view.showLoading();
        getUserSessionCase.invokeAsync(null, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                view.dismissLoading();

                if (user == null) {
                    view.toLogin();
                }
            }

            @Override
            public void onError(int i, String s) {
                view.dismissLoading();
                view.showFail(s);
            }
        });

    }

    public void onLogoutOrInButtonClick() {
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
                    view.toLogin();
                    return;
                }

                ResponseValue<Void> r = clearUserSessionCase.invokeSync(null);
                if (r.hasError()) {
                    view.dismissLoading();
                    view.showFail(r.getErrorMsg());
                    return;
                }

                view.dismissLoading();

                NoteApiManager.getINS().onUserLogout();

                view.showNick(context.getString(R.string.not_login));
                view.showLogout(context.getString(R.string.click_to_login));
                view.showContactText("");
                view.showHead(R.drawable.add_portrait);

            }
        });
    }

    public void onCheckAppUpdate() {
        view.showLoading();

        CheckAppUpdateCase.Params params = new CheckAppUpdateCase.Params();
        params.currentVersionCode = AppUtils.getAppVersionCode();

        checkAppUpdateCase.invokeAsync(params, new ControllerCallback<String>() {
            @Override
            public void onSuccess(@Nullable String s) {
                view.dismissLoading();

                view.showAppUpdateResult(s);
            }

            @Override
            public void onError(int i, String s) {
                view.dismissLoading();
                view.showFail(s);
            }
        });
    }
}
