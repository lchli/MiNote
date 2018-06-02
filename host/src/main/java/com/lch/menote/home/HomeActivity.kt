package com.lch.menote.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.lch.menote.R

import com.lch.menote.home.route.RouteCall
import com.lch.netkit.common.base.BaseCompatActivity
import com.lch.netkit.common.base.BaseFragment
import com.lch.netkit.common.base.FragmentAdapter
import com.lch.netkit.common.tool.ResUtils
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val adapter = FragmentAdapter(supportFragmentManager)

        val local = RouteCall.getNoteModule()?.localFrament(null)
        if (local != null) {
            adapter.addFragment(local, ResUtils.parseString(R.string.note))
        }

        val cloud = RouteCall.getNoteModule()?.cloudFragment(null)
        if (cloud != null) {
            adapter.addFragment(cloud, ResUtils.parseString(R.string.cloud_note))
        }

        val userFragment =RouteCall.getUserModule()?.indexPage(null)
        if (userFragment != null) {
            adapter.addFragment(userFragment, ResUtils.parseString(R.string.user))
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

        RouteCall.getNoteModule()?.onAppBackground(null)
    }

}
