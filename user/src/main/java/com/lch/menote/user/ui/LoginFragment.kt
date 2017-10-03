package com.lch.menote.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.common.route.NoteMod
import com.lch.menote.common.toast
import com.lch.menote.user.R
import com.lch.menote.user.data.DataSources
import com.lch.route.noaop.Android
import com.lch.route.noaop.lib.RouteEngine
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


/**
 * Created by lchli on 2016/8/10.
 */
class LoginFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        common_title.addRightText("register", {
            val userFragmentContainer = parentFragment as UserFragmentContainer
            userFragmentContainer.toRegister()
        })

        common_title.setCenterText("login", null)

        login_widget.setOnClickListener {
            loginAsync()
        }
    }


    private fun loginAsync() {
        val job = async(CommonPool) {
            try {
                val username = user_account_edit.text.toString()
                val pwd = user_pwd_edit.text.toString()

                val user = DataSources.net.getUser(username, pwd)
                if (user != null) {
                    DataSources.sp.addUser(user)

                } else {
                    Exception("login fail")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                e
            }

        }

        launch(Android) {
            val e = job.await()

            if (e is Exception) {
                getContext().toast(e.message)

            } else {
                val mod = RouteEngine.getModule(NoteMod.MODULE_NAME) as? NoteMod
                mod?.onUserLogin()

                val userFragmentContainer = parentFragment as UserFragmentContainer
                userFragmentContainer.toUserCenter(false)
            }


        }
    }

}