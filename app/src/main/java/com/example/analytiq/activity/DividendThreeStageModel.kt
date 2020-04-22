package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.analytiq.R
import java.text.DecimalFormat

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
    }

    fun calculateThree(view: View) {

        heading1.setVisibility(View.GONE)
        set.setVisibility(View.GONE)
        tableLayout.removeAllViews()

        Dividend.getText().clear()
        DF.getText().clear()
        PvOfDividend.getText().clear()
        PV.getText().clear()

        if (!TextUtils.isEmpty(RecentDividend.getText()) &&
            !TextUtils.isEmpty(FirstGrowthTime.getText()) &&
            !TextUtils.isEmpty(FirstGrowthRate.getText()) &&
            !TextUtils.isEmpty(SecondGrowthTime.getText()) &&
            !TextUtils.isEmpty(SecondGrowthRate.getText()) &&
            !TextUtils.isEmpty(FinalGrowthRate.getText()) &&
            !TextUtils.isEmpty(DiscountRate.getText())
        ) {

            val fgt: Int = Integer.parseInt(FirstGrowthTime.getText().toString())
            val sgt: Int = Integer.parseInt(SecondGrowthTime.getText().toString())

            val rd: Double = (RecentDividend.getText().toString()).toDouble()
            val fgr: Double = (FirstGrowthRate.getText().toString()).toDouble()
            val sgr: Double = (SecondGrowthRate.getText().toString()).toDouble()
            val fGr: Double = (FinalGrowthRate.getText().toString()).toDouble()
            val dr: Double = (DiscountRate.getText().toString()).toDouble()

            var dividnd = (RecentDividend.getText().toString()).toDouble()
            var de = 0.0
            var def = 0
            var dff = 0
            var pv = 0.0

            val decimalFormat = DecimalFormat("#.###")

            heading1.setVisibility(View.VISIBLE)
            set.setVisibility(View.VISIBLE)


            for (i in 0 until (FirstGrowthTime.getText().toString().toInt()) + (SecondGrowthTime.getText().toString().toInt())) {
                val tableRow = getLayoutInflater().inflate(R.layout.subthree, null) as TableRow
                tableLayout.addView(tableRow)


                val year = tableRow.getChildAt(0) as EditText
                year.setText((tableLayout.getChildCount()).toString())
                val dividend = tableRow.getChildAt(1) as EditText
                val extraValueET = tableRow.getChildAt(2) as EditText
                if (i != fgt + sgt - 1) {
                    extraValueET.setVisibility(View.INVISIBLE)
                }
                val df = tableRow.getChildAt(3) as EditText
                val pvfdv = tableRow.getChildAt(4) as EditText


                dff = Math.pow(
                    1 / ((dr / 100) + 1),
                    ((tableLayout.getChildCount()).toString().toDouble())
                )
                    .toInt()
                df.setText((decimalFormat.format(dff)))

                if (i < fgt) {

                    dividnd += dividnd * (fgr / 100) // Percent to decimal
                    dividend.setText(((decimalFormat.format(dividnd))))

                    val pvT = dividnd * dff
                    pv += pvT
                    pvfdv.setText((decimalFormat.format(pvT)))
                } else if (i >= fgt && i < fgt + sgt) {
                    dividnd += dividnd * sgr / 100
                    dividend.setText(((decimalFormat.format(dividnd))))

//                    dff = Math.pow(1 / (dr + 1),
//                            Double.parseDouble(String.valueOf(tableLayout.getChildCount())));
//                    df.setText(String.valueOf(decimalFormat.format(dff)));


                    if (i == fgt + sgt - 1) {
                        de = dividnd
                        def = dff
                    } else {
                        val pvT = dividnd * dff
                        pv += pvT
                        pvfdv.setText((decimalFormat.format(pvT)))
                    }
                } else {
                    dividnd += dividnd * (fGr / 100) // Percent to decimal
                    dividend.setText(((decimalFormat.format(dividnd))))

                    val extraValue = dividnd / ((dr - fGr) / 100)
                    // Create extraValueEditText
                    val tbr = tableLayout.getChildAt(i - 1) as TableRow
                    val evt = tbr.getChildAt(2) as EditText
                    evt.setText(((decimalFormat.format(extraValue))))

                    val pvT = (de + extraValue) * def
                    pv += pvT
                    val et = tbr.getChildAt(4) as EditText
                    et.setText((decimalFormat.format(pvT)))

                }
            }
            PV.setText((decimalFormat.format(pv)))
        } else {
            Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show()
        }
    }
}

