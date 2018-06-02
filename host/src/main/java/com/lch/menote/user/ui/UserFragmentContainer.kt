package com.lch.menote.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blankj.utilcode.util.ToastUtils
import com.lch.menote.R
import com.lch.menote.user.controller.UserController
import com.lch.netkit.common.base.BaseFragment

/**
 * Created by lchli on 2016/8/10.
 */
class UserFragmentContainer : BaseFragment() {

    private var mUserController = UserController()
    internal lateinit var userFragmentContainer: FrameLayout

    fun toRegister() {
        val trans = childFragmentManager.beginTransaction()
        trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)

        trans.replace(R.id.user_fragment_container, RegisterFragment())
        trans.commitAllowingStateLoss()
    }

    fun toLogin(isNeedAnim: Boolean) {
        val trans = childFragmentManager.beginTransaction()
        if (isNeedAnim) {
            trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        trans.replace(R.id.user_fragment_container, LoginFragment())
        trans.commitAllowingStateLoss()
    }

    fun toUserCenter(isNeedAnim: Boolean) {
        val trans = childFragmentManager.beginTransaction()
        if (isNeedAnim) {
            trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
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
        mUserController.getUserSession({
            if(it.hasError()){
                ToastUtils.showLong(it.errMsg())
            }else if(it.data==null){
                toLogin(false)
            }else{
                toUserCenter(false)
            }
        })

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