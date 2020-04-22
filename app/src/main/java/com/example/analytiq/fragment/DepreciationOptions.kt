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
import com.example.analytiq.activity.DepreciationStraightLine
import com.example.analytiq.activity.DepreciationWrittenDown

/**
 * A simple [Fragment] subclass.
 */
class DepreciationOptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_depreciation_options, container, false)


        view.findViewById<RelativeLayout>(R.id.straight_line_method_card).setOnClickListener {
            startActivity(Intent(activity as Context, DepreciationStraightLine::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.written_down_method_card).setOnClickListener {
            startActivity(Intent(activity as Context, DepreciationWrittenDown::class.java))
        }


        return view
    }


}
