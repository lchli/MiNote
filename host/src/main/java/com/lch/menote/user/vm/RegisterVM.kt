package com.lch.menote.user.vm

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lch.menote.file.FileModuleInjector
import com.lch.menote.note.NoteApiManager
import com.lch.menote.user.UserModuleInjector
import com.lch.menote.user.domain.RegisterUseCase
import com.lch.menote.user.route.User
import com.lchli.arch.clean.ControllerCallback

class RegisterVM: ViewModel() {
    private val mRegisterUseCase = RegisterUseCase(UserModuleInjector.getINS().provideRemoteUserDataSource(),
            UserModuleInjector.getINS().provideLocalUserDataSource(), FileModuleInjector.getINS().provideRemoteFileSource())

    private  val account= MutableLiveData<String>()
    private   val pwd=MutableLiveData<String>()
    private   val nick=MutableLiveData<String>()
    private   val fail=MutableLiveData<String>()
       val launchDestination=MutableLiveData<String>()
    private  val loading=MutableLiveData<Boolean>()

    fun register(): Unit {

        if (TextUtils.isEmpty(account.value) || TextUtils.isEmpty(pwd.value)) {
            fail.postValue("empty.")
            return
        }

        val p = RegisterUseCase.RegisterParams()
        p.userName = account.value
        p.userPwd = pwd.value
        p.userHeadPath = ""

        loading.postValue(true)

        mRegisterUseCase.invokeAsync(p, object : ControllerCallback<User> {
            override fun onSuccess(user: User?) {
                loading.postValue(false)

                if (user == null) {
                    fail.postValue("user is null.")
                    return
                }
                NoteApiManager.getINS().onUserLogin()
                launchDestination.postValue("user_center")
               // view.toUserCenter()
            }

            override fun onError(i: Int, s: String) {
                fail.postValue(s)
                loading.postValue(false)
            }
        })
    }

}