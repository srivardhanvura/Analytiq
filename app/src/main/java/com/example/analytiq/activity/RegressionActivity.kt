package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import com.example.analytiq.R
import com.example.analytiq.fragment.MultipleRegression
import com.example.analytiq.fragment.SimpleRegression

class RegressionActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_regression,SimpleRegression()).commit()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected=parent?.getItemAtPosition(position).toString()

        when(selected){
            "Simple Regression"->{
                supportFragmentManager.beginTransaction().replace(R.id.frame_regression,SimpleRegression()).commit()
            }
            "Multiple Regression"->{
                supportFragmentManager.beginTransaction().replace(R.id.frame_regression,MultipleRegression()).commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regression)

        val spinner: Spinner =findViewById(R.id.spinner_regression)
        val frame:FrameLayout=findViewById(R.id.frame_regression)

        val list=ArrayList<String>()
        list.add("Simple Regression")
        list.add("Multiple Regression")

        val adapter=ArrayAdapter(this,R.layout.spinner_item_2,list)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        spinner.adapter=adapter
        spinner.setOnItemSelectedListener(this)

//        val tabsLayout=findViewById<TabLayout>(R.id.regression_tabs)
//        val viewPager:ViewPager=findViewById(R.id.view_pager_regression)
//
//        val adapter=adapterClass(supportFragmentManager)
//        adapter.addFrag(SimpleRegression(),"Simple Regression")
//        adapter.addFrag(MultipleRegression(),"Multiple Regression")
//        viewPager.adapter=adapter
//        tabsLayout.setupWithViewPager(viewPager)
    }

//    class adapterClass(manager: FragmentManager):FragmentPagerAdapter(manager){
//
//        val listFrag= ArrayList<Fragment>()
//        val listTitle= ArrayList<String>()
//
//        override fun getItem(position: Int): Fragment {
//            return listFrag[position]
//        }
//
//        override fun getCount(): Int {
//            return listFrag.size
//        }
//
//        fun addFrag(frag:Fragment,name:String){
//            listFrag.add(frag)
//            listTitle.add(name)
//        }
//
//        override fun getPageTitle(position: Int): CharSequence? {
//            return listTitle[position]
//        }
//    }
}
