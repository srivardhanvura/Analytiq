package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.analytiq.R
import java.text.DecimalFormat
import android.widget.Toast
import android.widget.EditText

class DividendThreeStageModel : AppCompatActivity() {

    lateinit var RecentDividend: EditText
    lateinit var FirstGrowthTime: EditText
    lateinit var SecondGrowthTime: EditText
    lateinit var FirstGrowthRate: EditText
    lateinit var SecondGrowthRate: EditText
    lateinit var DiscountRate: EditText
    lateinit var FinalGrowthRate: EditText
    lateinit var DF: EditText
    lateinit var extraValue: EditText
    lateinit var PV: EditText
    lateinit var Dividend: EditText
    lateinit var PvOfDividend: EditText
    lateinit var set: LinearLayout
    lateinit var heading1: TableRow
    lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_stage_model)

        findViewById<ScrollView>(R.id.three_stage_scroll).isHorizontalScrollBarEnabled = true
        findViewById<ScrollView>(R.id.three_stage_scroll).isVerticalScrollBarEnabled = true

        RecentDividend = findViewById(R.id.RecDiv)
        FirstGrowthRate = findViewById(R.id.frGrwRate)
        FirstGrowthTime = findViewById(R.id.FGrthTime)
        SecondGrowthRate = findViewById(R.id.SecGrwRt)
        SecondGrowthTime = findViewById(R.id.SGrthTime)
        FinalGrowthRate = findViewById(R.id.FinGrwRt)
        DiscountRate = findViewById(R.id.Dsrate)
        PV = findViewById(R.id.FinPV)

        set = findViewById(R.id.set)

        DF = findViewById(R.id.dff)
        Dividend = findViewById(R.id.Div)
        PvOfDividend = findViewById(R.id.PVDivi)
        extraValue = findViewById(R.id.Extr)



        heading1 = findViewById(R.id.table_heading1)
        tableLayout = findViewById(R.id.table)
        val calculate=findViewById<Button>(R.id.calculate)

        calculate.setOnClickListener {
            heading1.visibility = View.GONE
            set.visibility = View.GONE
            tableLayout.removeAllViews()

            Dividend.text.clear()
            DF.text.clear()
            PvOfDividend.text.clear()
            PV.text.clear()

            if (!TextUtils.isEmpty(RecentDividend.text) &&
                !TextUtils.isEmpty(FirstGrowthTime.text) &&
                !TextUtils.isEmpty(FirstGrowthRate.text) &&
                !TextUtils.isEmpty(SecondGrowthTime.text) &&
                !TextUtils.isEmpty(SecondGrowthRate.text) &&
                !TextUtils.isEmpty(FinalGrowthRate.text) &&
                !TextUtils.isEmpty(DiscountRate.text)
            ) {

                var dividnd = RecentDividend.text.toString().toDouble()
                var de = 0.0
                var def = 0.0
                var dff = 0.0
                var pv = 0.0

                val rd = RecentDividend.text.toString().toDouble()
                val fgr = FirstGrowthRate.text.toString().toDouble()
                val sgr = SecondGrowthRate.text.toString().toDouble()
                val fGr = FinalGrowthRate.text.toString().toDouble()
                val dr = DiscountRate.text.toString().toDouble()

                val decimalFormat = DecimalFormat("#.###")

                val fgt = FirstGrowthTime.text.toString().toInt()
                val sgt = SecondGrowthTime.text.toString().toInt()


                heading1.visibility = View.VISIBLE
                set.visibility = View.VISIBLE


                for (i in 0 until FirstGrowthTime.text.toString().toInt() + SecondGrowthTime.text.toString().toInt() + 1) {
                    val tableRow = layoutInflater.inflate(R.layout.subthree, null) as TableRow
                    tableLayout.addView(tableRow)


                    val year = tableRow.getChildAt(0) as EditText
                    year.setText(tableLayout.childCount.toString())
                    val dividend = tableRow.getChildAt(1) as EditText
                    val extraValueET = tableRow.getChildAt(2) as EditText
                    if (i != fgt + sgt - 1) {
                        extraValueET.visibility = View.INVISIBLE
                    }
                    val df = tableRow.getChildAt(3) as EditText
                    val pvfdv = tableRow.getChildAt(4) as EditText


                    val childCount=tableLayout.childCount.toDouble()

                    dff = Math.pow((1 / (dr / 100 + 1)), childCount)
                    df.setText(decimalFormat.format(dff))

                    if (i < fgt) {

                        dividnd += dividnd * (fgr / 100)
                        dividend.setText(decimalFormat.format(dividnd))

                        val pvT = dividnd * dff
                        pv += pvT
                        pvfdv.setText(decimalFormat.format(pvT))
                    } else if (i >= fgt && i < fgt + sgt) {
                        dividnd += dividnd * sgr / 100
                        dividend.setText(decimalFormat.format(dividnd))

                        //                    dff = Math.pow(1 / (dr + 1),
                        //                            Double.parseDouble(String.valueOf(tableLayout.getChildCount())));
                        //                    df.setText(String.valueOf(decimalFormat.format(dff)));


                        if (i == fgt + sgt - 1) {
                            de = dividnd
                            def = dff
                        } else {
                            val pvT = dividnd * dff
                            pv += pvT
                            pvfdv.setText(decimalFormat.format(pvT))
                        }
                    } else {
                        dividnd += dividnd * (fGr / 100) // Percent to decimal
                        dividend.setText(decimalFormat.format(dividnd))

                        val extraValue = dividnd / ((dr - fGr) / 100) // Percent to decimal
                        // Create extraValueEditText
                        val tbr = tableLayout.getChildAt(i - 1) as TableRow
                        val evt = tbr.getChildAt(2) as EditText
                        evt.setText(decimalFormat.format(extraValue))

                        val pvT = (de + extraValue) * def
                        pv += pvT
                        val et = tbr.getChildAt(4) as EditText
                        et.setText(decimalFormat.format(pvT))
                    }
                }
                PV.setText(decimalFormat.format(pv))
            } else {
                Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

