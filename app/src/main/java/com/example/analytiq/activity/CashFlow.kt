package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.analytiq.R
import com.example.analytiq.fragment.MultipleCashFlow
import com.example.analytiq.fragment.SingleCashFlow
import com.google.android.material.tabs.TabLayout


class CashFlow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_flow)

        val cashFlowVP:ViewPager = findViewById(R.id.cash_flow_VP)
        val tabCashFlow:TabLayout=findViewById(R.id.tabs_cashflow)

        val adapter=myAdpaterClass(supportFragmentManager)
        adapter.addFragment(SingleCashFlow(),"Single Cash Flow")
        adapter.addFragment(MultipleCashFlow(),"Multiple Cash Flow")
        cashFlowVP.adapter=adapter
        tabCashFlow.setupWithViewPager(cashFlowVP)
    }

    class myAdpaterClass(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        val fraglist: MutableList<Fragment> = ArrayList()
        val titleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fraglist[position]
        }

        override fun getCount(): Int {
            return fraglist.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
        fun addFragment(frag:Fragment,title:String){
            fraglist.add(frag)
            titleList.add(title)
        }
    }
}
