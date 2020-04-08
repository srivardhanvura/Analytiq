package com.example.analytiq.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.analytiq.R

class Cone_Mensuration : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cone__mensuration, container, false)

        view.findViewById<ScrollView>(R.id.cone_scroll_view).isHorizontalScrollBarEnabled=false
        view.findViewById<ScrollView>(R.id.cone_scroll_view).isVerticalScrollBarEnabled=false

        val tsaCone: TextView = view.findViewById(R.id.tsa_cone)
        val lsaCone: TextView = view.findViewById(R.id.lsa_cone)
        val volCone: TextView = view.findViewById(R.id.vol_cone)
        val slantCone: TextView = view.findViewById(R.id.slant_cone)
        val coneRad: EditText = view.findViewById(R.id.cone_rad)
        val coneHei: EditText = view.findViewById(R.id.cone_hei)
        val calcCone: Button = view.findViewById(R.id.cone_btn)

        calcCone.setOnClickListener {

            if (coneRad.text.isEmpty() || coneHei.text.isEmpty()) {
                Toast.makeText(activity as Context, "Enter all fields properly", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val rad = coneRad.text.toString().toDouble()
                val hei = coneHei.text.toString().toDouble()
                val slant = Math.sqrt((rad * rad) + (hei * hei))

                volCone.text = "Volume: " + "%.3f".format((Math.PI * rad * rad * hei) / 3)
                lsaCone.text = "Curved Surface Area: " + "%.3f".format(Math.PI * rad * slant)
                tsaCone.text = "Toal Surface Area: " + "%.3f".format(Math.PI * rad * (rad + slant))
                slantCone.text = "Slant Height: " + "%.3f".format(slant)

                volCone.visibility = View.VISIBLE
                tsaCone.visibility = View.VISIBLE
                lsaCone.visibility = View.VISIBLE
                slantCone.visibility = View.VISIBLE
            }
        }

        return view
    }


}
