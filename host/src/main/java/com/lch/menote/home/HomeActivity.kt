package com.lch.menote.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.lch.menote.R
import com.lch.menote.note.NoteApiManager
import com.lch.menote.note.ui.HotNoteUi
import com.lch.menote.note.ui.LocalNoteUi
import com.lch.menote.user.ui.UserFragmentContainer
import com.lchli.utils.base.BaseCompatActivity
import com.lchli.utils.base.BaseFragment
import com.lchli.utils.base.FragmentAdapter
import com.lchli.utils.tool.ResUtils
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val adapter = FragmentAdapter(supportFragmentManager)

        adapter.addFragment(HotNoteUi(), "热榜")
        adapter.addFragment(LocalNoteUi(), getString(R.string.local_note))
        adapter.addFragment(UserFragmentContainer(), ResUtils.parseString(R.string.user))

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

        NoteApiManager.getINS().onAppBackground()
    }

}
