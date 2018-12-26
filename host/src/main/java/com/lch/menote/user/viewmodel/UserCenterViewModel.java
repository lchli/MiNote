package com.lch.menote.user.viewmodel;

import android.support.annotation.Nullable;

import com.lch.menote.file.FileModuleInjector;
import com.lch.menote.user.UserModuleInjector;
import com.lch.menote.user.domain.ClearUserSessionCase;
import com.lch.menote.user.domain.GetUserSessionCase;
import com.lch.menote.user.domain.UpdateUserContactCase;
import com.lch.menote.user.domain.UpdateUserHeadIconCase;
import com.lch.menote.user.route.User;
import com.lch.netkit.common.mvc.ControllerCallback;

import androidx.lifecycle.MutableLiveData;

/**
 * Created by Administrator on 2018/12/26.
 */

public class UserCenterViewModel {
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> failMsg = new MutableLiveData<>();
    public MutableLiveData<String> uploadHeadSuccessEvent = new MutableLiveData<>();
    public MutableLiveData<String> updateContactSuccessEvent = new MutableLiveData<>();
    public MutableLiveData<User> getUserSessionSuccessEvent = new MutableLiveData<>();
    public MutableLiveData<Boolean> nickClickEvent = new MutableLiveData<>();
    public MutableLiveData<Void> logoutClickEvent = new MutableLiveData<>();
    public MutableLiveData<Void> gotoLoginAction = new MutableLiveData<>();

    private final UpdateUserHeadIconCase updateUserHeadIconCase = new UpdateUserHeadIconCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource(), FileModuleInjector.getINS().provideRemoteFileSource());

    private final UpdateUserContactCase updateUserContactCase = new UpdateUserContactCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource());

    private final GetUserSessionCase getUserSessionCase = new GetUserSessionCase(UserModuleInjector.getINS().provideLocalUserDataSource());
    private final ClearUserSessionCase clearUserSessionCase = new ClearUserSessionCase(UserModuleInjector.getINS().provideLocalUserDataSource());

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

                uploadHeadSuccessEvent.postValue(user.headUrl);
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

                updateContactSuccessEvent.postValue(user.userContact);
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

        getUserSessionCase.invokeAsync(null, new ControllerCallback<User>() {
            @Override
            public void onSuccess(@Nullable User user) {
                loading.postValue(false);

                getUserSessionSuccessEvent.postValue(user);
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
                    nickClickEvent.postValue(false);
                } else {
                    nickClickEvent.postValue(true);
                }
            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });

    }

    public void onLogoutButtonClick() {
        loading.postValue(true);

        clearUserSessionCase.invokeAsync(null, new ControllerCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void vo) {
                loading.postValue(false);
                logoutClickEvent.postValue(null);

            }

            @Override
            public void onError(int i, String s) {
                loading.postValue(false);
                failMsg.postValue(s);
            }
        });

    }
}
