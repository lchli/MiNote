package com.lch.menote.user.viewmodel;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.lch.menote.R;
import com.lch.menote.file.FileModuleInjector;
import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.domain.CheckAppUpdateCase;
import com.lch.menote.user.domain.ClearUserSessionCase;
import com.lch.menote.user.domain.GetUserSessionCase;
import com.lch.menote.user.domain.UpdateUserContactCase;
import com.lch.menote.user.domain.UpdateUserHeadIconCase;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ControllerCallback;

import androidx.lifecycle.MutableLiveData;

/**
 * vm只负责更新ui状态模型即负责ui更新逻辑。
 * Created by Administrator on 2018/12/26.
 */

public class UserCenterViewModel {
    public final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public final MutableLiveData<String> failMsg = new MutableLiveData<>();
    public final MutableLiveData<Void> gotoLoginAction = new MutableLiveData<>();
    public final MutableLiveData<Void> onLogoutAction = new MutableLiveData<>();
    public final MutableLiveData<String> userNickTextVM = new MutableLiveData<>();
    public final MutableLiveData<String> logoutTextVM = new MutableLiveData<>();
    public final MutableLiveData<String> userContactTextVM = new MutableLiveData<>();
    public final MutableLiveData<String> headImagePathVM = new MutableLiveData<>();
    public final MutableLiveData<String> appVersionTextVM = new MutableLiveData<>();
    public final MutableLiveData<Integer> headImageResVM = new MutableLiveData<>();
    public final MutableLiveData<String> appUpdateResult = new MutableLiveData<>();

    private final UpdateUserHeadIconCase updateUserHeadIconCase = new UpdateUserHeadIconCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource(), FileModuleInjector.getINS().provideRemoteFileSource());

    private final UpdateUserContactCase updateUserContactCase = new UpdateUserContactCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource());

    private final GetUserSessionCase getUserSessionCase = new GetUserSessionCase(UserModuleInjector.getINS().provideLocalUserDataSource());
    private final ClearUserSessionCase clearUserSessionCase = new ClearUserSessionCase(UserModuleInjector.getINS().provideLocalUserDataSource());
    private final CheckAppUpdateCase checkAppUpdateCase = new CheckAppUpdateCase(UserModuleInjector.getINS().provideAppUpdateInfoDataSource());
    private Context context;

    public UserCenterViewModel(Context context) {
        this.context = context;
    }

    public void onModifyUserHead(String headPath) {
        loading.postValue(true);

        UpdateUserHeadIconCase.UpdateParams params = new UpdateUserHeadIconCase.UpdateParams();
        params.headPath = headPath;

        updateUserHeadIconCase.invokeAsync(params, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                loading.postValue(false);

                if (user == null) {
                    failMsg.postValue("user is null.");
                    return;
                }

                headImagePathVM.postValue(user.headUrl);
            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });
    }


    public void onModifyUserContact(String contact) {
        loading.postValue(true);

        UpdateUserContactCase.UpdateParams params = new UpdateUserContactCase.UpdateParams();
        params.userContact = contact;

        updateUserContactCase.invokeAsync(params, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                loading.postValue(false);

                if (user == null) {
                    failMsg.postValue("user is null.");
                    return;
                }

                userContactTextVM.postValue(user.userContact);
            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });
    }


    public void onLoadUserInfo() {
        loading.postValue(true);
        appVersionTextVM.postValue(context.getString(R.string.app_version_pattern, AppUtils.getAppVersionName()));

        getUserSessionCase.invokeAsync(null, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                loading.postValue(false);

                if (user == null) {
                    userNickTextVM.postValue(context.getString(R.string.not_login));
                    logoutTextVM.postValue(context.getString(R.string.click_to_login));
                    userContactTextVM.postValue("");
                    headImageResVM.postValue(R.drawable.add_portrait);

                } else {
                    userNickTextVM.postValue(user.name);
                    logoutTextVM.postValue(context.getString(R.string.logout));
                    if (TextUtils.isEmpty(user.userContact)) {
                        userContactTextVM.postValue("联系方式:未填写");
                    } else {
                        userContactTextVM.postValue("联系方式:" + user.userContact);
                    }
                    headImagePathVM.postValue(user.headUrl);
                }
            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });
    }

    public void onNickButtonClick() {
        loading.postValue(true);

        getUserSessionCase.invokeAsync(null, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                loading.postValue(false);

                if (user == null) {
                    gotoLoginAction.postValue(null);
                }
            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });

    }

    public void onLogoutOrInButtonClick() {
        loading.postValue(true);

        getUserSessionCase.invokeAsync(null, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                if (user == null) {
                    loading.postValue(false);
                    gotoLoginAction.postValue(null);
                } else {
                    clearUserSessionCase.invokeAsync(null, new ControllerCallback<Void>() {
                        @Override
                        public void onSuccess(@Nullable Void vo) {
                            loading.postValue(false);

                            onLogoutAction.postValue(null);

                            userNickTextVM.postValue(context.getString(R.string.not_login));
                            logoutTextVM.postValue(context.getString(R.string.click_to_login));
                            userContactTextVM.postValue("");
                            headImageResVM.postValue(R.drawable.add_portrait);

                        }

                        @Override
                        public void onError(int i, String s) {
                            loading.postValue(false);
                            failMsg.postValue(s);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });


    }

    public void onCheckAppUpdate() {
        loading.postValue(true);

        CheckAppUpdateCase.Params params = new CheckAppUpdateCase.Params();
        params.currentVersionCode = AppUtils.getAppVersionCode();

        checkAppUpdateCase.invokeAsync(params, new ControllerCallback<String>() {
            @Override
            public void onSuccess(@Nullable String s) {
                loading.postValue(false);

                appUpdateResult.postValue(s);
            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });
    }
}
