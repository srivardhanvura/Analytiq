package com.example.analytiq.fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.analytiq.R
import com.example.analytiq.activity.CurrencyConversion
import com.example.analytiq.activity.LCM_HCF
import com.example.analytiq.activity.ProfitActivity
import com.example.analytiq.activity.ProportionActivity
import com.example.analytiq.adapter.HomeAdapter
import com.example.analytiq.model.HomeListData

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val listOfItems = ArrayList<HomeListData>()
        listOfItems.add(HomeListData(R.drawable.button1, "Depreciation", R.drawable.a))
        listOfItems.add(HomeListData(R.drawable.button2, "Interest", R.drawable.b))
        listOfItems.add(HomeListData(R.drawable.button4, "Present Value", R.drawable.d))
        listOfItems.add(HomeListData(R.drawable.button6, "NPV & IRR", R.drawable.f))
        listOfItems.add(HomeListData(R.drawable.button5, "Mensuration", R.drawable.e))
        listOfItems.add(HomeListData(R.drawable.button3, "Critical Value", R.drawable.c))
        listOfItems.add(HomeListData(R.drawable.button7, "Mean & SD", R.drawable.g))
        listOfItems.add(HomeListData(R.drawable.button8, "Correlation & Regression", R.drawable.h))
        listOfItems.add(HomeListData(R.drawable.button, "Var Calculation", R.drawable.j))
        listOfItems.add(HomeListData(R.drawable.button2, "Equity Pricing", R.drawable.k1))
        listOfItems.add(HomeListData(R.drawable.button4, "Power of Number", R.drawable.l))
//        listOfItems.add(HomeListData(R.drawable.button6, "Bond Valuation", R.drawable.n))
        listOfItems.add(HomeListData(R.drawable.button5, "Discounting Factor", R.drawable.m))
        listOfItems.add(HomeListData(R.drawable.button3, "YTM", R.drawable.i))
        listOfItems.add(HomeListData(R.drawable.button6, "Duration of Bond", R.drawable.clock))
        listOfItems.add(HomeListData(R.drawable.button8, "Normal Distribution", R.drawable.normal))
        listOfItems.add(HomeListData(R.drawable.button, "EMI", R.drawable.emi))

        val adapter = HomeAdapter(activity as Context,listOfItems,activity as Activity)
        val layout = LinearLayoutManager(activity as Context)
        val recycler=view.findViewById<RecyclerView>(R.id.home_recycler)
        recycler.adapter=adapter
        recycler.layoutManager=layout

        view.findViewById<Button>(R.id.hcf_btn).setOnClickListener {
            startActivity(Intent(activity as Context,LCM_HCF::class.java))
        }

        view.findViewById<Button>(R.id.profit_btn).setOnClickListener {
            startActivity(Intent(activity as Context,ProfitActivity::class.java))
        }

        view.findViewById<Button>(R.id.proportion_btn).setOnClickListener {
            startActivity(Intent(activity as Context,ProportionActivity::class.java))
        }

        view.findViewById<Button>(R.id.currency_btn).setOnClickListener {
            startActivity(Intent(activity as Context,CurrencyConversion::class.java))
        }

        return view
    }

}
