package com.example.analytiq.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import com.example.analytiq.R
import android.widget.Toast
import android.text.TextUtils
import android.widget.Button
import java.text.DecimalFormat


class LogNormalVarFragment : Fragment() {

    lateinit var amountOfPortfolio: EditText
    lateinit var mean: EditText
    lateinit var sdET: EditText
    lateinit var varET: EditText
    lateinit var criticalValue: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_log_normal_var, container, false)

        view.findViewById<ScrollView>(R.id.log_normal_scroll).isHorizontalScrollBarEnabled = false
        view.findViewById<ScrollView>(R.id.log_normal_scroll).isVerticalScrollBarEnabled = false

        amountOfPortfolio = view.findViewById(R.id.a)
        mean = view.findViewById(R.id.m)
        sdET = view.findViewById(R.id.sd)
        criticalValue = view.findViewById(R.id.cv)
        varET = view.findViewById(R.id.`var`)
        val calculate = view.findViewById<Button>(R.id.calculate)

        calculate.setOnClickListener(View.OnClickListener {
            val decimalFormat = DecimalFormat("#.###")

            if (!TextUtils.isEmpty(amountOfPortfolio.text) && !TextUtils.isEmpty(mean.text) && !TextUtils.isEmpty(
                    sdET.text
                ) && !TextUtils.isEmpty(criticalValue.text)
            ) {

                val a: Double
                val m: Double
                val sd: Double
                val cv: Double
                val var1: Double

                a = java.lang.Double.parseDouble(amountOfPortfolio.text.toString())
                m = java.lang.Double.parseDouble(mean.text.toString())
                sd = java.lang.Double.parseDouble(sdET.text.toString())
                cv = java.lang.Double.parseDouble(criticalValue.text.toString())

                var1 = a * (1 - Math.exp(m / 100 - sd / 100 * cv))

                varET.setText((decimalFormat.format(var1)).toString())
            } else {
                Toast.makeText(context, "Fill Properly", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }


}
