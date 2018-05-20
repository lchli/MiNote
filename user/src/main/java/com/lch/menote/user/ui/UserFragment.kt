package com.lch.menote.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.user.R
import com.lch.menote.user.data.DI
import com.lch.menote.user.route.RouteCall
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * Created by lchli on 2016/8/10.
 */
class UserFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_user, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout_widget.setOnClickListener {
            DI.provideSpSource().removeCurrentUser()

            val mod = RouteCall.getNoteModule()
            mod?.onUserLogout(null)

            val userFragmentContainer = parentFragment as UserFragmentContainer
            userFragmentContainer.toLogin(true)
        }

        val session = DI.provideSpSource().getUser()

        if (session != null) {
            user_nick.text = session.userName
        }
    }
}