package com.example.analytiq.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.example.analytiq.R
import com.example.analytiq.activity.*

/**
 * A simple [Fragment] subclass.
 */
class BasicMathsOptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_basic_maths_options, container, false)

        view.findViewById<RelativeLayout>(R.id.hcf_card).setOnClickListener {
            startActivity(Intent(activity as Context,LCM_HCF::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.profit_card).setOnClickListener {
            startActivity(Intent(activity as Context,ProfitActivity::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.proportion_card).setOnClickListener {
            startActivity(Intent(activity as Context,ProportionActivity::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.linear_card).setOnClickListener {
            startActivity(Intent(activity as Context,LinearEquation::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.unit_card).setOnClickListener {
            startActivity(Intent(activity as Context,UnitConverter::class.java))
        }

        return view
    }


}
