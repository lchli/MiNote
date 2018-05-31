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
import com.lch.netkit.common.mvc.ControllerCallback
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * Created by lchli on 2016/8/10.
 */
class UserFragment : BaseFragment() {
    private var mUserController = UserController()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_user, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout_widget.setOnClickListener {

            mUserController.clearUserSession({})

            val mod = RouteCall.getNoteModule()
            mod?.onUserLogout(null)

            val userFragmentContainer = parentFragment as UserFragmentContainer
            userFragmentContainer.toLogin(true)
        }

        mUserController.getUserSession({
            if (it.hasError() || it.data == null) {
                ToastUtils.showLong(it.errMsg())
            } else {
                user_nick.text = it.data.name
            }
        })

    }
}