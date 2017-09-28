package com.lch.menote.user

import android.content.Context
import android.support.v4.app.Fragment
import com.lch.menote.common.launchActivity
import com.lch.menote.common.route.UserMod
import com.lch.menote.user.data.UserRepo
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService
import com.lch.route.noaop.lib.Router
import kotlin.properties.Delegates

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService(UserMod.MODULE_NAME)
class UserModule : Router, UserMod {

    private var mContext: Context by Delegates.notNull()

    override fun init(context: Context) {
        mContext = context
    }

    @RouteMethod(UserMod.INDEX)
    override fun indexPage(params: Map<String, String>?): Fragment {
        return UserFragment()
    }

    @RouteMethod(UserMod.PWD_PAGE)
    override fun lockPwdPage(params: Map<String, String>?) {
        mContext.launchActivity(LockPwdActivity::class.java)
    }

    @RouteMethod(UserMod.GET_LOCK_PWD)
    override fun getLockPwd(params: Map<String, String>?): String {
        return UserRepo.getLockPwd()!!
    }

    override fun onAppBackground(params: Map<String, String>?) {
        UserRepo.clearLockPwd()
    }
}