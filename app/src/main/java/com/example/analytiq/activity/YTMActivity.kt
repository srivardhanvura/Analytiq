package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.analytiq.R
import com.example.analytiq.fragment.NonPlainVanillaBond
import com.example.analytiq.fragment.PerpetualBond
import com.example.analytiq.fragment.PlainVanillaBond
import com.example.analytiq.fragment.ZeroCouponBond
import com.google.android.material.tabs.TabLayout

class YTMActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ytm)

        val viewPager:ViewPager=findViewById(R.id.ytm_VP)
        val tabs:TabLayout=findViewById(R.id.ytm_tabs)

        val adapter=adapterClass(supportFragmentManager)
        adapter.addFrag(ZeroCouponBond(),"Zero Coupon Bond")
        adapter.addFrag(PlainVanillaBond(),"Plain Vanilla Bond")
        adapter.addFrag(NonPlainVanillaBond(),"Non Plain Vanilla Bond")
        adapter.addFrag(PerpetualBond(),"Perpetual Bond")
        viewPager.adapter=adapter
        tabs.setupWithViewPager(viewPager)
    }

    class adapterClass(manager:FragmentManager):FragmentPagerAdapter(manager){

        val fragList=ArrayList<Fragment>()
        val titleList=ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragList[position]
        }

        override fun getCount(): Int {
            return fragList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

        fun addFrag(frag:Fragment, title:String){
            fragList.add(frag)
            titleList.add(title)
        }

    }
}
