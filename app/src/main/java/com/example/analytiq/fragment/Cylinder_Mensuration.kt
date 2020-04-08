package com.example.analytiq.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.analytiq.R

class Cylinder_Mensuration : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cylinder__mensuration, container, false)

        view.findViewById<ScrollView>(R.id.cylin_scroll_view).isHorizontalScrollBarEnabled=false
        view.findViewById<ScrollView>(R.id.cylin_scroll_view).isVerticalScrollBarEnabled=false

        val tsaCylin: TextView = view.findViewById(R.id.tsa_cylin)
        val lsaCylin: TextView = view.findViewById(R.id.lsa_cylin)
        val volCylin: TextView = view.findViewById(R.id.vol_cylin)
        val cylinRad: EditText = view.findViewById(R.id.cylinder_rad)
        val cylinHei: EditText = view.findViewById(R.id.cylinder_hei)
        val calcCylin: Button = view.findViewById(R.id.cylin_btn)

        calcCylin.setOnClickListener {

            if (cylinRad.text.isEmpty() || cylinHei.text.isEmpty()) {
                Toast.makeText(activity as Context, "Enter all fields properly", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val rad = cylinRad.text.toString().toDouble()
                val hei = cylinHei.text.toString().toDouble()

                volCylin.text = "Volume: " +"%.3f".format (Math.PI * rad * rad * hei)
                lsaCylin.text = "Curved Surface Area: " + "%.3f".format(Math.PI * 2 * rad * hei)
                tsaCylin.text = "Toal Surface Area: " +"%.3f".format (Math.PI * 2 * rad * (rad + hei))

                volCylin.visibility = View.VISIBLE
                tsaCylin.visibility = View.VISIBLE
                lsaCylin.visibility = View.VISIBLE
            }
        }

        return view
    }


}
