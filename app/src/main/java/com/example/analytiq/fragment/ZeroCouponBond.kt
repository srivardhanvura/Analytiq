package com.example.analytiq.fragment

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


class ZeroCouponBond : Fragment() {

    lateinit var presentValue:EditText
    lateinit var futureValue: EditText
    lateinit var ytm:EditText
    lateinit var time:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_zero_coupon_bond, container, false)

        view.findViewById<ScrollView>(R.id.zero_coupon_scroll).isHorizontalScrollBarEnabled=false
        view.findViewById<ScrollView>(R.id.zero_coupon_scroll).isVerticalScrollBarEnabled=false

        presentValue = view.findViewById(R.id.pv)
        futureValue = view.findViewById(R.id.fv)
        ytm = view.findViewById(R.id.ytm)
        time = view.findViewById(R.id.t)
        val calculate : Button = view.findViewById(R.id.calculate)

        calculate.setOnClickListener {
            val decimalFormat = DecimalFormat("#.###")

            if (!TextUtils.isEmpty(presentValue.text) && !TextUtils.isEmpty(futureValue.text) && !TextUtils.isEmpty(
                    time.text
                )
            ) {

                val pv: Double
                val fv: Double
                val y: Double
                val t: Double

                pv = java.lang.Double.parseDouble(presentValue.text.toString())
                t = java.lang.Double.parseDouble(time.text.toString())
                fv = java.lang.Double.parseDouble(futureValue.text.toString())

                y = Math.pow(fv / pv, 1 / t) - 1

                ytm.setText((decimalFormat.format(y * 100)).toString())
            } else {
                Toast.makeText(context, "Fill Properly", Toast.LENGTH_SHORT).show()
            }
        }

        return view

    }


}
