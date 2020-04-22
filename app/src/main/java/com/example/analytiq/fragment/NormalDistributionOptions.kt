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
import com.example.analytiq.activity.NormalDistriActivity
import com.example.analytiq.activity.NormalDistriWithMean

/**
 * A simple [Fragment] subclass.
 */
class NormalDistributionOptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_normal_distribution_options,container,false)

        view.findViewById<RelativeLayout>(R.id.standard_card).setOnClickListener {
            startActivity(Intent(activity as Context,NormalDistriActivity::class.java))
        }

        view.findViewById<RelativeLayout>(R.id.normal_card).setOnClickListener {
            startActivity(Intent(activity as Context,NormalDistriWithMean::class.java))
        }

        return view
    }


}
