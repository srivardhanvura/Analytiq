package com.example.analytiq.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.analytiq.R


class Cuboid_Mensuration : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cuboid__mensuration, container, false)

        view.findViewById<ScrollView>(R.id.cuboid_scroll_view).isHorizontalScrollBarEnabled=false
        view.findViewById<ScrollView>(R.id.cuboid_scroll_view).isVerticalScrollBarEnabled=false

        val lenEdit: EditText = view.findViewById(R.id.cuboid_len)
        val breEdit: EditText = view.findViewById(R.id.cuboid_bre)
        val heiEdit: EditText = view.findViewById(R.id.cuboid_hei)
        val tsaCuboid: TextView = view.findViewById(R.id.tsa_cuboid)
        val lsaCuboid: TextView = view.findViewById(R.id.lsa_cuboid)
        val volCuboid: TextView = view.findViewById(R.id.vol_cuboid)
        val calCuboid: Button = view.findViewById(R.id.cuboid_btn)

        calCuboid.setOnClickListener {

            if (lenEdit.text.isEmpty() || breEdit.text.isEmpty() || heiEdit.text.isEmpty()) {
                Toast.makeText(
                    activity as Context,
                    "Please enter all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val length = lenEdit.text.toString().toDouble()
                val breadth = breEdit.text.toString().toDouble()
                val height = heiEdit.text.toString().toDouble()
                val tsa=2*((length*breadth)+(breadth*height)+(height*length))
                val lsa=2*(length+breadth)*height
                tsaCuboid.text="Total Surface Area: "+"%.3f".format( tsa)
                lsaCuboid.text="Lateral Surface Area: "+ "%.3f".format(lsa)
                volCuboid.text="Volume: "+ "%.3f".format(length*breadth*height)

                tsaCuboid.visibility=View.VISIBLE
                lsaCuboid.visibility=View.VISIBLE
                volCuboid.visibility=View.VISIBLE
            }
        }
        return view
    }


}
