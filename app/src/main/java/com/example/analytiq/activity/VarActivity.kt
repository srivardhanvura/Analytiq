package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.analytiq.R
import com.example.analytiq.fragment.LogNormalVarFragment
import com.example.analytiq.fragment.NormalVarFragment
import com.google.android.material.tabs.TabLayout

class VarActivity : AppCompatActivity() {

    lateinit var var_viewPager:ViewPager
    lateinit var var_tabs:TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_var)

        var_viewPager=findViewById(R.id.var_VP)
        var_tabs=findViewById(R.id.var_tabs)

        val adapter=myAdapter(supportFragmentManager)
        adapter.addFrag(NormalVarFragment(),"Normal Var")
        adapter.addFrag(LogNormalVarFragment(),"LogNormal Var")
        var_viewPager.adapter=adapter
        var_tabs.setupWithViewPager(var_viewPager)


    }
    class myAdapter(manager: FragmentManager):FragmentPagerAdapter(manager){

        val listFrag=ArrayList<Fragment>()
        val listTitle=ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return listFrag[position]
        }

        override fun getCount(): Int {
            return listFrag.size
        }

        fun addFrag(frag:Fragment,name:String){
            listFrag.add(frag)
            listTitle.add(name)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return listTitle[position]
        }
    }
}

