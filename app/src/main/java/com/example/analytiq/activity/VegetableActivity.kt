package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.analytiq.R
import com.example.analytiq.fragment.Vegetable1
import com.example.analytiq.fragment.Vegetable2
import com.google.android.material.tabs.TabLayout

class VegetableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vegetable)

        val vp=findViewById<ViewPager>(R.id.viewPager)
        val tabs=findViewById<TabLayout>(R.id.tab)

        val adapter=adap(supportFragmentManager)
        adapter.addFrag("Price Calculator",Vegetable1())
        adapter.addFrag("Quantity Calculator",Vegetable2())
        vp.adapter=adapter
        tabs.setupWithViewPager(vp)
    }

    class adap(anager:FragmentManager):FragmentPagerAdapter(anager){

        val listNames=ArrayList<String>()
        val listFrag=ArrayList<Fragment>()

        override fun getItem(position: Int): Fragment {
            return listFrag[position]
        }

        override fun getCount(): Int {
            return listFrag.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return listNames[position]
        }

        fun addFrag(name:String,frag:Fragment){
            listFrag.add(frag)
            listNames.add(name)
        }
    }
}
