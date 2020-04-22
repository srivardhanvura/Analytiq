package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.analytiq.R
import com.example.analytiq.fragment.HCFFragment
import com.example.analytiq.fragment.LCMFragment
import com.google.android.material.tabs.TabLayout

class LCM_HCF : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lcm__hcf)

        val tabs=findViewById<TabLayout>(R.id.lcm_hcf_tab)
        val viewPager=findViewById<ViewPager>(R.id.hcf_lcm_viewpager)

        val adapter=adapterClass(supportFragmentManager)
        adapter.addFrag(LCMFragment(),"LCM")
        adapter.addFrag(HCFFragment(),"HCF")
        viewPager.adapter=adapter
        tabs.setupWithViewPager(viewPager)
    }

    class adapterClass(manager:FragmentManager):FragmentPagerAdapter(manager){

        val listItem=ArrayList<Fragment>()
        val listName=ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return listItem[position]
        }

        override fun getCount(): Int {
            return listItem.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return listName[position]
        }

        fun addFrag(frag:Fragment,name:String){
            listItem.add(frag)
            listName.add(name)
        }
    }
}
