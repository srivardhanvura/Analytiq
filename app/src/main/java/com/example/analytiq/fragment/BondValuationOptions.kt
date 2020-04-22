package com.example.analytiq.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView

import com.example.analytiq.R
import com.example.analytiq.activity.YTMActivity

class BondValuationOptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bond_valuation_options, container, false)

        view.findViewById<RelativeLayout>(R.id.bond_valuation_card).setOnClickListener {
            startActivity(Intent(activity as Context, YTMActivity::class.java))
        }

        return view
    }


}
