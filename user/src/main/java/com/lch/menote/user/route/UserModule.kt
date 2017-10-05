package com.lch.menote.user.route

import android.content.Context
import android.support.v4.app.Fragment
import com.lch.menote.common.launchActivity
import com.lch.menote.common.route.UserMod
import com.lch.menote.common.route.model.User
import com.lch.menote.user.LockPwdActivity
import com.lch.menote.user.data.DataSources
import com.lch.menote.user.ui.UserFragmentContainer
import com.lch.route.noaop.lib.RouteMethod
import com.lch.route.noaop.lib.RouteService
import com.lch.route.noaop.lib.Router
import kotlin.properties.Delegates

/**
 * Created by Administrator on 2017/9/21.
 */
@RouteService(UserMod.MODULE_NAME)
class UserModule : Router, UserMod {

    companion object {
        const val SP = "user-sp"
    }

    private var mContext: Context by Delegates.notNull()

    override fun init(context: Context) {
        mContext = context
    }

    @RouteMethod(UserMod.INDEX)
    override fun indexPage(params: Map<String, String>?): Fragment {
        return UserFragmentContainer()
    }

    @RouteMethod(UserMod.PWD_PAGE)
    override fun lockPwdPage(params: Map<String, String>?) {
        mContext.launchActivity(LockPwdActivity::class.java)
    }

    @RouteMethod(UserMod.GET_LOCK_PWD)
    override fun getLockPwd(params: Map<String, String>?): String {
        return DataSources.mem.getLockPwd()!!
    }

    override fun onAppBackground(params: Map<String, String>?) {
        DataSources.mem.clearLockPwd()
    }

    override fun userSession(): User? {
        val user = DataSources.sp.getUser()

        return user
    }

    override fun queryUser(userId: String): User? {

        try {
            val user = DataSources.net.getUser(userId)
            return user
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}