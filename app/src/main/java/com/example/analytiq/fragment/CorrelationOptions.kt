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
import com.example.analytiq.activity.CorrelationActivity
import com.example.analytiq.activity.RegressionActivity

/**
 * A simple [Fragment] subclass.
 */
class CorrelationOptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_correlation_options, container, false)

        view.findViewById<RelativeLayout>(R.id.correlation_card).setOnClickListener {
            startActivity(Intent(activity as Context, CorrelationActivity::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.regression_card).setOnClickListener {
            startActivity(Intent(activity as Context, RegressionActivity::class.java))
        }
        return view
    }


}
