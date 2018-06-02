package com.lch.menote.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.lch.menote.R
import com.lch.menote.user.controller.UserController
import com.lch.menote.user.route.RouteCall
import com.lch.menote.user.route.User
import com.lch.netkit.common.base.BaseFragment
import com.lch.netkit.common.mvc.ControllerCallback
import com.lch.netkit.common.mvc.ResponseValue
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * Created by lchli on 2016/8/10.
 */
class LoginFragment : BaseFragment() {

    val userController= UserController()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        common_title.addRightText(getString(R.string.user_goto_register_text), {
            val userFragmentContainer = parentFragment as UserFragmentContainer
            userFragmentContainer.toRegister()
        })

        common_title.setCenterText("", null)

        login_widget.setOnClickListener {
            loginAsync()
        }
    }


    private fun loginAsync() {
        val username = user_account_edit.text.toString()
        val pwd = user_pwd_edit.text.toString()


        userController.login(username,pwd, object : ControllerCallback<User> {

            override fun onComplete(res: ResponseValue<User>) {
                if(res.hasError()||res.data==null){
                    ToastUtils.showLong(res.errMsg())
                    return
                }

                val mod = RouteCall.getNoteModule()
                mod?.onUserLogin(null)

                val userFragmentContainer = parentFragment as UserFragmentContainer
                userFragmentContainer.toUserCenter(false)

            }
        })

    }

}