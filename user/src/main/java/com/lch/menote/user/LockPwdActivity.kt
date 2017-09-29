package com.lch.menote.user

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.lch.menote.common.logIfDebug
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
        val numbers = Array(patternLockView.dotCount) { IntArray(patternLockView.dotCount) }
        var i = 0
        for (childArr in numbers) {
            for (index in childArr.indices) {
                childArr[index] = i++
            }
        }

        patternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                if (pattern == null || pattern.size < 1) {
                    toast("密码至少6位")
                    return
                }
                var inputPwd = ""

                for (dot in pattern) {
                    inputPwd += numbers[dot.row][dot.column]
                }
                logIfDebug("inputPwd:$inputPwd")

                UserRepo.saveLockPwd(inputPwd)
                RouteEngine.route(HomeModulePaths.ROUTE_PATH_HOME)
                finish()

            }

            override fun onCleared() {
            }

            override fun onStarted() {
            }

            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
            }
        })

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
