package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.analytiq.R
import android.widget.Toast
import android.widget.EditText
import android.text.TextUtils
import android.view.View
import java.text.DecimalFormat


class DurationInYears : AppCompatActivity() {

    lateinit var futureValue:EditText
    lateinit var Ytm:EditText
    lateinit var couponRate:EditText
    lateinit var yearsToMaturity:EditText
    lateinit var spv:EditText
    lateinit var swx:EditText
    lateinit var duration:EditText
    lateinit var currentPrice:EditText
    lateinit var set:LinearLayout
    lateinit var heading1:TableRow
    lateinit var heading2:TableRow
    lateinit var tableLayout:TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duration_in_years)

        findViewById<ScrollView>(R.id.duration_years_scroll).isHorizontalScrollBarEnabled=false
        findViewById<ScrollView>(R.id.duration_years_scroll).isVerticalScrollBarEnabled=false

        futureValue = findViewById(R.id.fv)
        couponRate = findViewById(R.id.c)
        yearsToMaturity = findViewById(R.id.ym)
        Ytm = findViewById(R.id.ytm)
        set = findViewById(R.id.set)
        spv = findViewById(R.id.spv)
        swx = findViewById(R.id.swx)
        currentPrice = findViewById(R.id.cp)
        duration = findViewById(R.id.d)
        heading1 = findViewById(R.id.table_heading1)
        heading2 = findViewById(R.id.table_heading2)
        tableLayout = findViewById(R.id.table)

    }

    fun calculate(view: View)
    {

        heading1.visibility = View.GONE
        heading2.visibility = View.GONE
        set.visibility = View.GONE
        tableLayout.removeAllViews()

        spv.text.clear()
        swx.text.clear()
        currentPrice.text.clear()
        duration.text.clear()


        if (!TextUtils.isEmpty(futureValue.text) && !TextUtils.isEmpty(couponRate.text) && !TextUtils.isEmpty(
                yearsToMaturity.text
            ) && !TextUtils.isEmpty(Ytm.text)
        ) {


            val fv: Double
            val c: Double
            val ym: Double
            val ytm: Double

            var currentprice = 0.0
            var SWX = 0.0
            fv = (futureValue.text.toString()).toDouble()
            c =(couponRate.text.toString()).toDouble()
            ym = (yearsToMaturity.text.toString()).toDouble()
            ytm =(Ytm.text.toString()).toDouble()

            val decimalFormat = DecimalFormat("#.###")

            heading1.visibility = View.VISIBLE
            heading2.visibility = View.VISIBLE
            set.visibility = View.VISIBLE


            for (i in 0 until Integer.parseInt(yearsToMaturity.text.toString())) {
                val tableRow =
                    layoutInflater.inflate(R.layout.duration_in_years_rows, null) as TableRow
                tableLayout.addView(tableRow)

                val year = tableRow.getChildAt(0) as EditText
                val cashflow = tableRow.getChildAt(1) as EditText
                val presentValue = tableRow.getChildAt(2) as EditText
                val WX = tableRow.getChildAt(3) as EditText

                year.setText(tableLayout.childCount.toString())
                val cf: Double
                if (i == Integer.parseInt(yearsToMaturity.text.toString()) - 1) {
                    cf = fv + fv * c / 100
                    cashflow.setText((decimalFormat.format(cf)).toString())
                } else {
                    cf = fv * c / 100
                    cashflow.setText((decimalFormat.format(cf)).toString())
                }

                val w = cf / Math.pow(
                    1 + ytm / 100,
                    java.lang.Double.parseDouble(tableLayout.childCount.toString())
                )

                currentprice += w
                presentValue.setText((decimalFormat.format(w)).toString())

                val wx = java.lang.Double.parseDouble(tableLayout.childCount.toString()) * w

                SWX += wx

                WX.setText((decimalFormat.format(wx)).toString())
            }


            spv.setText((decimalFormat.format(currentprice)).toString())
            currentPrice.setText((decimalFormat.format(currentprice)).toString())
            swx.setText((decimalFormat.format(SWX)).toString())
            duration.setText((decimalFormat.format(SWX / currentprice)).toString())
        } else {
            Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show()
        }
    }
}
