package com.lch.menote.user

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.lch.menote.common.route.HomeModulePaths
import com.lch.menote.common.route.NoteMod
import com.lch.menote.common.showListDialog
import com.lch.menote.common.toast
import com.lch.menote.user.data.UserRepo
import com.lch.route.noaop.lib.RouteEngine
import com.orhanobut.dialogplus.OnItemClickListener
import kotlinx.android.synthetic.main.activity_pwd.*

class LockPwdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwd)

        if (TextUtils.isEmpty(UserRepo.getLockPwd())) {
            etLockPwd.hint = "请设置解锁密码"
        }
    }

    fun confirm(v: View) {
        val inputPwd = etLockPwd.text.toString()
        if (inputPwd.length < 6) {
            toast("密码至少6位")
            return
        }

        UserRepo.saveLockPwd(inputPwd)
        RouteEngine.route(HomeModulePaths.ROUTE_PATH_HOME)
        finish()
    }

    fun reset(v: View) {

        showListDialog(OnItemClickListener { dialog, item, view, position ->
            if (position == 1) {
                (RouteEngine.getModule(NoteMod.MODULE_NAME) as? NoteMod)?.clearDB()
                toast("重置成功")
            }
            dialog.dismiss()

        }, items = listOf("重置会导致所有数据丢失，是否确认？", "确认"))


    }
}
