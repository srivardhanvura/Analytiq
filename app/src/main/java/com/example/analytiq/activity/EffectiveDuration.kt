package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import com.example.analytiq.R
import android.widget.EditText
import android.widget.Toast
import android.text.TextUtils
import android.view.View
import java.text.DecimalFormat

class EffectiveDuration : AppCompatActivity() {

    lateinit var futureValue: EditText
    lateinit var couponRate: EditText
    lateinit var yearsToMaturity: EditText
    lateinit var Ytm: EditText
    lateinit var volatility: EditText
    lateinit var med: EditText
    lateinit var mp0: EditText
    lateinit var mp1: EditText
    lateinit var mp2: EditText
    lateinit var p0c: EditText
    lateinit var p0fa: EditText
    lateinit var p0a: EditText
    lateinit var p0f: EditText
    lateinit var p1c: EditText
    lateinit var p1fa: EditText
    lateinit var p1a: EditText
    lateinit var p1f: EditText
    lateinit var p2c: EditText
    lateinit var p2fa: EditText
    lateinit var p2f: EditText
    lateinit var p2a: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_effective_duration)

        findViewById<ScrollView>(R.id.effective_duration_scroll).isHorizontalScrollBarEnabled =
            false
        findViewById<ScrollView>(R.id.effective_duration_scroll).isVerticalScrollBarEnabled = false

        futureValue = findViewById(R.id.fv)
        couponRate = findViewById(R.id.c)
        yearsToMaturity = findViewById(R.id.ym)
        Ytm = findViewById(R.id.ytm)
        med = findViewById(R.id.ed)
        volatility = findViewById(R.id.v)
        mp0 = findViewById(R.id.p0)
        mp1 = findViewById(R.id.p1)
        mp2 = findViewById(R.id.p2)
        p0c = findViewById(R.id.p0c)
        p0fa = findViewById(R.id.p0fa)
        p0a = findViewById(R.id.p0a)
        p0f = findViewById(R.id.p0f)
        p1c = findViewById(R.id.p1c)
        p1fa = findViewById(R.id.p1fa)
        p1a = findViewById(R.id.p1a)
        p1f = findViewById(R.id.p1f)
        p2c = findViewById(R.id.p2c)
        p2fa = findViewById(R.id.p2fa)
        p2f = findViewById(R.id.p2f)
        p2a = findViewById(R.id.p2a)

    }

    fun calculate(view: View) {

        if (!TextUtils.isEmpty(futureValue.text) && !TextUtils.isEmpty(couponRate.text) && !TextUtils.isEmpty(
                yearsToMaturity.text
            ) && !TextUtils.isEmpty(Ytm.text) && !TextUtils.isEmpty(volatility.text)
        ) {

            val fv: Double
            val c: Double
            val ytm: Double
            val v: Double

            fv = java.lang.Double.parseDouble(futureValue.text.toString())
            c = java.lang.Double.parseDouble(couponRate.text.toString())
            ytm = java.lang.Double.parseDouble(Ytm.text.toString())
            v = java.lang.Double.parseDouble(volatility.text.toString())

            val decimalFormat = DecimalFormat("#.###")

            var spv0 = 0.0
            var spv1 = 0.0
            var spv2 = 0.0
            var pv0n = 0.0
            var pv1n = 0.0
            var pv2n = 0.0


            for (i in 0 until Integer.parseInt(yearsToMaturity.text.toString())) {

                val pv0: Double
                pv0 = 1 / Math.pow(1 + ytm / 100, (i + 1).toDouble())
                if (i == Integer.parseInt(yearsToMaturity.text.toString()) - 1) {
                    pv0n = pv0
                }
                spv0 += pv0

                val pv1: Double
                pv1 = 1 / Math.pow(1 + (ytm + v) / 100, (i + 1).toDouble())
                if (i == Integer.parseInt(yearsToMaturity.text.toString()) - 1) {
                    pv1n = pv1
                }
                spv1 += pv1
                val pv2: Double
                pv2 = 1 / Math.pow(1 + (ytm - v) / 100, (i + 1).toDouble())
                if (i == Integer.parseInt(yearsToMaturity.text.toString()) - 1) {
                    pv2n = pv2
                }
                spv2 += pv2

            }

            val p0 = fv * c * spv0 / 100 + fv * pv0n
            val p1 = fv * c * spv1 / 100 + fv * pv1n
            val p2 = fv * c * spv2 / 100 + fv * pv2n
            val ed = (p2 - p1) / (2.0 * p0 * v / 100)

            med.setText((decimalFormat.format(ed)) + " %")
            mp0.setText((decimalFormat.format(p0)))
            mp1.setText((decimalFormat.format(p1)))
            mp2.setText((decimalFormat.format(p2)))

            p0c.setText((decimalFormat.format(fv * c / 100)))
            p0fa.setText((decimalFormat.format(spv0)))
            p0a.setText((decimalFormat.format(fv)))
            p0f.setText((decimalFormat.format(pv0n)))
            p1c.setText((decimalFormat.format(fv * c / 100)))
            p1fa.setText((decimalFormat.format(spv1)))
            p1a.setText((decimalFormat.format(fv)))
            p1f.setText((decimalFormat.format(pv1n)))
            p2c.setText((decimalFormat.format(fv * c / 100)))
            p2fa.setText((decimalFormat.format(spv2)))
            p2f.setText((decimalFormat.format(pv2n)))
            p2a.setText((decimalFormat.format(fv)))
        } else {
            Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show()
        }
    }

}
