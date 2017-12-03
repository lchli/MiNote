package com.lch.menote.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.lch.menote.common.base.BaseAppCompatActivity
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.common.base.FragmentAdapter
import com.lch.menote.common.netkit.NetKit
import com.lch.menote.common.route.NoteMod
import com.lch.menote.common.route.NoteModulePaths
import com.lch.menote.common.route.UserMod
import com.lch.menote.common.util.ResUtils
import com.lch.route.noaop.lib.RouteEngine
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val adapter = FragmentAdapter(supportFragmentManager)

        val local = RouteEngine.route(NoteModulePaths.ROUTE_PATH_LOCAL_NOTE) as? Fragment
        if (local != null) {
            adapter.addFragment(local, ResUtils.parseString(R.string.note))
        }

        val cloud = RouteEngine.route(NoteModulePaths.ROUTE_PATH_CLOUD_NOTE) as? Fragment
        if (cloud != null) {
            adapter.addFragment(cloud, ResUtils.parseString(R.string.cloud_note))
        }

        val userMod = RouteEngine.getModule(UserMod.MODULE_NAME) as? UserMod
        if (userMod != null) {
            adapter.addFragment(userMod.indexPage(), ResUtils.parseString(R.string.user))
        }

        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = adapter.count

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }


            override fun onPageSelected(position: Int) {

                val fragmentAdapter = viewpager.adapter as FragmentAdapter

                val fragment = fragmentAdapter.getItem(position) as BaseFragment

                if (!fragment.isInitLoadDataCalled) {

                    fragment.isInitLoadDataCalled = true

                    fragment.initLoadData()

                }

            }


            override fun onPageScrollStateChanged(state: Int) {


            }

        })

        tabs.tabMode = TabLayout.MODE_FIXED
        tabs.setupWithViewPager(viewpager)




    }


    override fun onSaveInstanceState(outState: Bundle) {
        //Override and do not save.
    }

    override fun onDestroy() {
        super.onDestroy()
        (RouteEngine.getModule(NoteMod.MODULE_NAME) as? NoteMod)?.onAppBackground()
    }

}
