package com.lch.menote.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.common.route.model.User
import com.lch.menote.user.R
import com.lch.menote.user.data.DataSources

/**
 * Created by lchli on 2016/8/10.
 */
class UserFragmentContainer : BaseFragment() {


    internal lateinit var userFragmentContainer: FrameLayout

    fun toRegister() {
        val trans = childFragmentManager.beginTransaction()
        trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        trans.replace(R.id.user_fragment_container, RegisterFragment())
        trans.commitAllowingStateLoss()
    }

    fun toLogin(isNeedAnim: Boolean) {
        val trans = childFragmentManager.beginTransaction()
        if (isNeedAnim) {
            trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        trans.replace(R.id.user_fragment_container, LoginFragment())
        trans.commitAllowingStateLoss()
    }

    fun toUserCenter(isNeedAnim: Boolean) {
        val trans = childFragmentManager.beginTransaction()
        if (isNeedAnim) {
            trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        trans.replace(R.id.user_fragment_container, UserFragment())
        trans.commitAllowingStateLoss()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_user_container, container, false)
        userFragmentContainer = view.findViewById(R.id.user_fragment_container) as FrameLayout
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initLoadData() {
        var session: User? = null

        try {
            session = DataSources.sp.getUser()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (session == null) {
            toLogin(false)
        } else {
            toUserCenter(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        //Override and do not save.
    }

    companion object {

        fun newInstance(): UserFragmentContainer {

            val args = Bundle()
            val fragment = UserFragmentContainer()
            fragment.arguments = args
            return fragment
        }
    }
}