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
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HCFFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hcf, container, false)

        val firstEt: EditText = view.findViewById(R.id.hcfFirstEt)
        val calculate: Button = view.findViewById(R.id.calculate)
        val resText: TextView = view.findViewById(R.id.res_text)

        calculate.setOnClickListener {
            val first = firstEt.text.toString()

            if (first.isEmpty())
                Toast.makeText(activity as Context, "Fill properly", Toast.LENGTH_SHORT).show()
            else {
                val listText = first.split(" ")
                val listNum = ArrayList<Int>()
                for (i in 0 until listText.size) {
                    val value = listText[i].toIntOrNull()
                    if (value != null) {
                        listNum.add(value)
                    }
                }
                val l = Collections.min(listNum)
                if (l == 0)
                    Toast.makeText(activity as Context, "Zero should not be entered", Toast.LENGTH_SHORT).show()
                else {
                    var k: Int = l
                    for (i in l downTo 0) {
                        var flag = 0
                        for (j in 0 until listNum.size) {
                            if (listNum[j] % i != 0) {
                                flag = 1
                                break
                            }
                        }
                        if (flag == 0) {
                            k = i
                            break
                        }
                    }
                    resText.setText("HCF of " + listNum+" : " + k)
                    resText.visibility = View.VISIBLE
                }
            }
        }

        return view
    }


}
