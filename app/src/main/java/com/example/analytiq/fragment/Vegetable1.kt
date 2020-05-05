package com.example.analytiq.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.analytiq.R

class Vegetable1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vegetable1, container, false)

        val a = view.findViewById<EditText>(R.id.editText2)
        val b = view.findViewById<EditText>(R.id.editText3)
        val c = view.findViewById<EditText>(R.id.editText4)
        val asd = view.findViewById<TextView>(R.id.textView6)
        val calculate = view.findViewById<Button>(R.id.button)
        calculate.setOnClickListener {

            if (a.text.isEmpty() || b.text.isEmpty() || c.text.isEmpty()) {
                Toast.makeText(activity as Context, "Fill properly", Toast.LENGTH_SHORT).show()
            } else {

                val x = a.text.toString()
                val y = b.text.toString()
                val z = c.text.toString()
                val x1 = x.toDouble()
                val x2 = y.toDouble()
                val x3 = z.toDouble()
                val x4: Double?
                x4 = x3 * x2 / x1
                asd.setText("%.3f".format(x4))
                view.findViewById<TextView>(R.id.textView5).visibility=View.VISIBLE
                view.findViewById<TextView>(R.id.textView6).visibility=View.VISIBLE
            }
        }
        return view
    }
}
