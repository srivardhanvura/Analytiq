package com.example.analytiq.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.analytiq.R
import android.widget.EditText
import android.widget.Toast
import android.text.TextUtils
import android.widget.Button
import java.text.DecimalFormat

class SingleCashFlow : Fragment() {

    var pv: Double = 0.toDouble()
    var a: Double = 0.toDouble()
    var r: Double = 0.toDouble()
    var t: Double = 0.toDouble()

    lateinit var presentValue: EditText
    lateinit var amount: EditText
    lateinit var interestRate: EditText
    lateinit var totalTime: EditText
    lateinit var calculate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_single_cash_flow, container, false)

        presentValue = view.findViewById(R.id.pv)
        amount = view.findViewById(R.id.a)
        interestRate = view.findViewById(R.id.r)
        totalTime = view.findViewById(R.id.t)
        calculate = view.findViewById(R.id.calculate)

        calculate.setOnClickListener {
            val decimalFormat = DecimalFormat("#.###")

            if (TextUtils.isEmpty(presentValue.text) || TextUtils.isEmpty(amount.text) || TextUtils.isEmpty(
                    interestRate.text
                ) || TextUtils.isEmpty(totalTime.text)
            ) {

                val cashFlowCalculation = CashFlowCalculation()

                if (!TextUtils.isEmpty(presentValue.text)) {
                    pv = java.lang.Double.parseDouble(presentValue.text.toString())
                }
                if (!TextUtils.isEmpty(amount.text)) {
                    a = java.lang.Double.parseDouble(amount.text.toString())
                }
                if (!TextUtils.isEmpty(interestRate.text)) {
                    r = java.lang.Double.parseDouble(interestRate.text.toString())
                }
                if (!TextUtils.isEmpty(totalTime.text)) {
                    t = java.lang.Double.parseDouble(totalTime.text.toString())
                }


                if (TextUtils.isEmpty(presentValue.text)) {
                    pv = cashFlowCalculation.calcFinalAmount(a, r, t)
                    presentValue.setText((decimalFormat.format(pv)).toString())
                }
                if (TextUtils.isEmpty(amount.text)) {
                    a = cashFlowCalculation.calcAmount(pv, r, t)
                    amount.setText((decimalFormat.format(a)).toString())
                }
                if (TextUtils.isEmpty(interestRate.text)) {
                    r = cashFlowCalculation.calcInterestRate(pv, a, t)
                    interestRate.setText((decimalFormat.format(r)).toString())
                }
                if (TextUtils.isEmpty(totalTime.text)) {
                    t = cashFlowCalculation.calcTimePeriod(pv, a, r)
                    totalTime.setText((decimalFormat.format(t)).toString())
                }

            } else {
                Toast.makeText(context, "Fill Properly", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    class CashFlowCalculation {

        fun calcFinalAmount(a: Double, r: Double, t: Double): Double {
            return a / Math.pow(1 + r / 100, t)
        }

        fun calcAmount(pv: Double, r: Double, t: Double): Double {

            return pv * Math.pow(1 + r / 100, t)
        }

        fun calcTimePeriod(pv: Double, a: Double, r: Double): Double {

            return Math.log(a / pv) / Math.log(1 + r / 100)
        }

       fun calcInterestRate(pv: Double, a: Double, t: Double): Double {
            return 100 * (Math.exp(Math.log(a / pv) / t) - 1)
        }

    }
}
