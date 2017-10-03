package com.lch.menote.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.common.route.NoteMod
import com.lch.menote.user.R
import com.lch.menote.user.data.DataSources
import com.lch.route.noaop.lib.RouteEngine
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
            DataSources.sp.removeCurrentUser()

            val mod = RouteEngine.getModule(NoteMod.MODULE_NAME) as? NoteMod
            mod?.onUserLogout()

            val userFragmentContainer = parentFragment as UserFragmentContainer
            userFragmentContainer.toLogin(true)
        }

        val session = DataSources.sp.getUser()

        if (session != null) {
            user_nick.text = session.userName
        }
    }
}