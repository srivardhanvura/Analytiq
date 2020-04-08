package com.example.analytiq.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.analytiq.R

class Sphere_Mensuration : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sphere__mensuration, container, false)

        view.findViewById<ScrollView>(R.id.sphere_scroll_view).isHorizontalScrollBarEnabled=false
        view.findViewById<ScrollView>(R.id.sphere_scroll_view).isVerticalScrollBarEnabled=false

        val sphereRad: EditText = view.findViewById(R.id.sphere_rad)
        val tsaSphere: TextView = view.findViewById(R.id.tsa_sphere)
        val volSphere: TextView = view.findViewById(R.id.vol_sphere)
        val calcSphere: Button = view.findViewById(R.id.sphere_btn)

        calcSphere.setOnClickListener {

            if (sphereRad.text.isEmpty()) {
                Toast.makeText(activity as Context, "Enter all fields properly", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val rad = sphereRad.text.toString().toDouble()

                tsaSphere.text = "Total Surface Area: " + "%.3f".format(Math.PI * 4 * rad * rad)
                volSphere.text = "Volume: " + "%.3f".format((Math.PI * 4 * rad * rad * rad) / 3)

                volSphere.visibility=View.VISIBLE
                tsaSphere.visibility=View.VISIBLE
            }
        }

        return view
    }


}
