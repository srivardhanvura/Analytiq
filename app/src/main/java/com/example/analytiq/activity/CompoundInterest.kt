package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.analytiq.R


class CompoundInterest : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var a: Double? = null
    var p: Double? = null
    var r: Double? = null
    var n: Double? = null
    var t: Double? = null
    lateinit var compoundFreq: String

    lateinit var finalAmount: EditText
    lateinit var initialPrinciple: EditText
    lateinit var interestRate: EditText
    lateinit var customCompoundFrequency: EditText
    lateinit var compoundFrequency: Spinner
    lateinit var totalTime: EditText
    lateinit var compoundInterest: TextView
    lateinit var freqAdapter1: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compound_interest)

        findViewById<ScrollView>(R.id.compound_scroll_view).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.compound_scroll_view).isVerticalScrollBarEnabled = false

        finalAmount = findViewById(R.id.a)
        initialPrinciple = findViewById(R.id.p)
        interestRate = findViewById(R.id.r)
        compoundFrequency = findViewById(R.id.n)
        totalTime = findViewById(R.id.t)
        compoundInterest = findViewById(R.id.x)
        customCompoundFrequency = findViewById(R.id.nCustom)

        val compoundFreqArray = ArrayList<String>()
        compoundFreqArray.add(0, "Compound Frequency")
        compoundFreqArray.add("Annually")
        compoundFreqArray.add("Semiannually")
        compoundFreqArray.add("Quarterly")
        compoundFreqArray.add("Monthly")
        compoundFreqArray.add("Daily")
        compoundFreqArray.add("Continuous")
        compoundFreqArray.add("Custom")

        freqAdapter1 = ArrayAdapter(this, R.layout.spinner_item, compoundFreqArray)
        freqAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        compoundFrequency.setAdapter(freqAdapter1)
        compoundFrequency.setOnItemSelectedListener(this)

    }

    private fun calcFinalAmount(p: Double?, r: Double?, n: Double?, t: Double?): Double {

        return if (compoundFreq == "Continuous") {
            p!! * Math.exp(r!! / 100 * t!!)
        } else {

            p!! * Math.pow(1 + r!! / (100 * n!!), n * t!!)

        }
    }


    private fun calcInitialPrinciple(a: Double?, r: Double?, n: Double?, t: Double?): Double {
        return if (compoundFreq == "Continuous") {
            a!! / Math.exp(r!! / 100 * t!!)

        } else {

            a!! / Math.pow(1 + r!! / (100 * n!!), n * t!!)

        }
    }

    private fun calcInterestRate(a: Double?, p: Double?, n: Double?, t: Double?): Double {
        return if (compoundFreq == "Continuous") {

            100 * (Math.log(a!! / p!!) / t!!)

        } else {
            100.0 * n!! * (Math.exp(Math.log(a!! / p!!) / (n * t!!)) - 1)
        }
    }


    private fun calcTimePeriod(a: Double?, p: Double?, r: Double?, n: Double?): Double {
        return if (compoundFreq == "Continuous") {
            Math.log(a!! / p!!) / (r!! / 100)

        } else {
            Math.log(a!! / p!!) / (n!! * Math.log(1 + r!! / (100 * n)))
        }
    }


    private fun calcCompoundInterest(a: Double?, p: Double?): Double {

        return a!! - p!!
    }

    fun calculate(view: View) {
        var c=0

        if(finalAmount.text.isEmpty())
            c++
        if(initialPrinciple.text.isEmpty())
            c++
        if(totalTime.text.isEmpty())
            c++
        if(interestRate.text.isEmpty())
            c++
        if(c>3){
            Toast.makeText(this, "Please fill properly", Toast.LENGTH_SHORT).show()
            return
        }

        if (n != null) {
            if (compoundFreq == "Custom" && TextUtils.isEmpty(customCompoundFrequency.text)) {
                Toast.makeText(this, "Please fill compounding frequency", Toast.LENGTH_SHORT).show()
            } else if (compoundFreq == "Custom" && java.lang.Double.parseDouble(
                    customCompoundFrequency.text.toString()
                ) <= 0
            ) {
                Toast.makeText(this, "Please fill valid Compound frequency", Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(finalAmount.text) || TextUtils.isEmpty(initialPrinciple.text) || TextUtils.isEmpty(
                interestRate.text
            ) || TextUtils.isEmpty(totalTime.text)
        ) {

            if (!TextUtils.isEmpty(customCompoundFrequency.text)) {
                n = java.lang.Double.parseDouble(customCompoundFrequency.text.toString())
            }
            if (!TextUtils.isEmpty(finalAmount.text)) {
                a = java.lang.Double.parseDouble(finalAmount.text.toString())
            }
            if (!TextUtils.isEmpty(initialPrinciple.text)) {
                p = java.lang.Double.parseDouble(initialPrinciple.text.toString())
            }
            if (!TextUtils.isEmpty(interestRate.text)) {
                r = java.lang.Double.parseDouble(interestRate.text.toString())
            }
            if (!TextUtils.isEmpty(totalTime.text)) {
                t = java.lang.Double.parseDouble(totalTime.text.toString())
            }


            if (TextUtils.isEmpty(finalAmount.text)) {
                a = calcFinalAmount(p, r, n, t)
                finalAmount.setText(a.toString())
            }
            if (TextUtils.isEmpty(initialPrinciple.text)) {
                p = calcInitialPrinciple(a, r, n, t)
                initialPrinciple.setText(p.toString())
            }
            if (TextUtils.isEmpty(interestRate.text)) {
                r = calcInterestRate(a, p, n, t)
                interestRate.setText(r.toString())
            }
            if (TextUtils.isEmpty(totalTime.text)) {
                t = calcTimePeriod(a, p, r, n)
                totalTime.setText(t.toString())
            }

//            compoundInterest.text = "Compound Interest =" + calcCompoundInterest(a, p).toString()

        } else {
            Toast.makeText(
                this,
                "Fill Properly or all the fields are filled, clear a field",
                Toast.LENGTH_SHORT
            ).show()
        }
    }else
    {
        Toast.makeText(
            this@CompoundInterest,
            "Please select compound frequency",
            Toast.LENGTH_LONG
        ).show()
    }
}

override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    compoundFreq = parent?.getItemAtPosition(position).toString()

//        finalAmount.setText("")
//        totalTime.setText("")
//        interestRate.setText("")
//        initialPrinciple.setText("")

    if (compoundFreq == "Annually") {
        customCompoundFrequency.visibility = View.GONE
        customCompoundFrequency.text.clear()
        n = 1.0
    } else if (compoundFreq == "Semiannually") {
        customCompoundFrequency.visibility = View.GONE
        customCompoundFrequency.text.clear()
        n = 2.0
    } else if (compoundFreq == "Quarterly") {
        customCompoundFrequency.visibility = View.GONE
        customCompoundFrequency.text.clear()
        n = 4.0
    } else if (compoundFreq == "Monthly") {
        customCompoundFrequency.visibility = View.GONE
        customCompoundFrequency.text.clear()
        n = 12.0
    } else if (compoundFreq == "Daily") {
        customCompoundFrequency.visibility = View.GONE
        customCompoundFrequency.text.clear()
        n = 365.0
    } else if (compoundFreq == "Continuous") {
        customCompoundFrequency.visibility = View.GONE
        customCompoundFrequency.text.clear()
        n = 0.0
    } else if (compoundFreq == "Compound Frequency") {
        customCompoundFrequency.visibility = View.GONE
        customCompoundFrequency.text.clear()
    } else {
        customCompoundFrequency.visibility = View.VISIBLE
    }
}

override fun onNothingSelected(parent: AdapterView<*>?) {

}
}
