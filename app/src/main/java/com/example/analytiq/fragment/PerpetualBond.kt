package com.example.analytiq.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import com.example.analytiq.R
import android.widget.Toast
import android.text.TextUtils
import java.text.DecimalFormat


class PerpetualBond : Fragment() {

    lateinit var yield1:EditText
    lateinit var currentMarketPrice:EditText
    lateinit var couponAmount: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_perpetual_bond, container, false)

        view.findViewById<ScrollView>(R.id.perpetual_scroll).isHorizontalScrollBarEnabled=false
        view.findViewById<ScrollView>(R.id.perpetual_scroll).isVerticalScrollBarEnabled=false

        yield1 = view.findViewById(R.id.yield)
        currentMarketPrice = view.findViewById(R.id.cmp)
        couponAmount = view.findViewById(R.id.ca)
        val calculate:Button = view.findViewById(R.id.calculate)

        calculate.setOnClickListener {
            val decimalFormat = DecimalFormat("#.###")

            if (!TextUtils.isEmpty(currentMarketPrice.text) && !TextUtils.isEmpty(couponAmount.text)) {

                val cmp: Double
                val fv: Double
                val y: Double
                val ca: Double

                cmp = java.lang.Double.parseDouble(currentMarketPrice.text.toString())
                ca = java.lang.Double.parseDouble(couponAmount.text.toString())

                y = ca / cmp

                yield1.setText((decimalFormat.format(y * 100)).toString())
            } else {
                Toast.makeText(activity as Context, "Fill Properly", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


}
