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
import com.example.analytiq.activity.CompoundInterest
import com.example.analytiq.activity.SimpleInterest

/**
 * A simple [Fragment] subclass.
 */
class InterestOptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_interest_options, container, false)

        view.findViewById<RelativeLayout>(R.id.simple_interest_card).setOnClickListener {
            startActivity(Intent(activity as Context,SimpleInterest::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.compound_interest_card).setOnClickListener {
            startActivity(Intent(activity as Context,CompoundInterest::class.java))
        }

        return view
    }


}
