package com.example.analytiq.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.analytiq.R

class Cube_Mensuration : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cube__mensuration, container, false)

        view.findViewById<ScrollView>(R.id.cube_scroll_view).isHorizontalScrollBarEnabled=false
        view.findViewById<ScrollView>(R.id.cube_scroll_view).isVerticalScrollBarEnabled=false

        val cubeLength: EditText = view.findViewById(R.id.cube_side)
        val tsa_cube: TextView = view.findViewById(R.id.tsa_cube)
        val lsa_cube: TextView = view.findViewById(R.id.lsa_cube)
        val vol_cube: TextView = view.findViewById(R.id.vol_cube)
        val calcCube: Button = view.findViewById(R.id.cube_btn)

        calcCube.setOnClickListener {

            if (!cubeLength.text.isEmpty()) {
                val edge = cubeLength.text.toString().toDouble()
                tsa_cube.text = "Total Surface Area: " + "%.3f".format(6 * edge * edge)
                lsa_cube.text = "Lateral Surface Area: " + "%.3f".format(4 * edge * edge)
                vol_cube.text = "Volume: " + "%.3f".format(edge * edge * edge)

                tsa_cube.visibility = View.VISIBLE
                lsa_cube.visibility = View.VISIBLE
                vol_cube.visibility = View.VISIBLE
            } else {
                Toast.makeText(activity as Context, "Enter a valid length", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view
    }


}
