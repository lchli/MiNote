package com.lch.menote.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.lch.menote.common.base.BaseAppCompatActivity
import com.lch.menote.common.base.BaseFragment
import com.lch.menote.common.util.ResUtils
import com.lch.menote.home.route.RouteCall
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val adapter = com.lch.menote.common.base.FragmentAdapter(supportFragmentManager)

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

                val fragmentAdapter = viewpager.adapter as com.lch.menote.common.base.FragmentAdapter

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
