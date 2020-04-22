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
import com.example.analytiq.activity.MeanVarianceSD

/**
 * A simple [Fragment] subclass.
 */
class MeanVarianceSDOptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_mean_variance_sdoptions, container, false)

        view.findViewById<RelativeLayout>(R.id.mean_card).setOnClickListener {
            startActivity(Intent(activity as Context,MeanVarianceSD::class.java))
        }

        return view
    }


}
