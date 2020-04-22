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
import com.example.analytiq.activity.NPVIRRActivity

/**
 * A simple [Fragment] subclass.
 */
class NPV_IRROptions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_npv_irroptions, container, false)

        view.findViewById<RelativeLayout>(R.id.npv_card).setOnClickListener {
            startActivity(Intent(activity as Context,NPVIRRActivity::class.java))
        }

        return view
    }


}
