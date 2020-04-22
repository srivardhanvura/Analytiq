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
class EquityPricingOptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_equity_pricing_options, container, false)

        view.findViewById<RelativeLayout>(R.id.double_growth_card).setOnClickListener {
            startActivity(Intent(activity as Context, DividendTwoStageMode::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.triple_growth_card).setOnClickListener {
            startActivity(Intent(activity as Context, DividendThreeStageModel::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.single_growth_card).setOnClickListener {
            startActivity(Intent(activity as Context, ConstantGrowthActivity::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.no_growth_card).setOnClickListener {
            startActivity(Intent(activity as Context, ZeroGrowthActivity::class.java))
        }

        return view
    }


}
