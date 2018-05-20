package com.lch.menote.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.common.toast
import com.lch.menote.user.R
import com.lch.menote.user.data.DI
import com.lch.menote.user.route.RouteCall
import com.lch.route.noaop.Android
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * Created by lchli on 2016/8/10.
 */
class RegisterFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        common_title.addRightText("login", {
            val userFragmentContainer = parentFragment as UserFragmentContainer
            userFragmentContainer.toLogin(true)
        })

        common_title.setCenterText("register", null)

        register_widget.setOnClickListener {
            registerAsync()
        }
    }


    private fun registerAsync() {
        val job = async(CommonPool) {
            try {
                val username = user_account_edit.text.toString()
                val pwd = user_pwd_edit.text.toString()

                val user = DI.provideNetSource().addUser(username, pwd)
                if (user != null) {
                    DI.provideSpSource().addUser(user)

                } else {
                    Exception("register fail")
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
                val mod = RouteCall.getNoteModule()
                mod?.onUserLogin(null)

                val userFragmentContainer = parentFragment as UserFragmentContainer
                userFragmentContainer.toUserCenter(false)
            }


        }
    }

}