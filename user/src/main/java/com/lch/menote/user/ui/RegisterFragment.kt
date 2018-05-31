package com.lch.menote.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.user.R
import com.lch.menote.user.controller.UserController
import com.lch.menote.user.route.RouteCall
import com.lch.menote.userapi.User
import com.lch.netkit.common.mvc.ControllerCallback
import com.lch.netkit.common.mvc.ResponseValue
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * Created by lchli on 2016/8/10.
 */
class RegisterFragment : BaseFragment() {

    val userController = UserController()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        common_title.addRightText(getString(R.string.user_goto_login_text), {
            val userFragmentContainer = parentFragment as UserFragmentContainer
            userFragmentContainer.toLogin(true)
        })

        common_title.setCenterText("", null)

        register_widget.setOnClickListener {
            registerAsync()
        }
    }


    private fun registerAsync() {
        val username = user_account_edit.text.toString()
        val pwd = user_pwd_edit.text.toString()

        userController.register(username, pwd, object : ControllerCallback<User> {

            override fun onComplete(res: ResponseValue<User>) {
                if (res.hasError() || res.data == null) {
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