package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import com.example.analytiq.R
import android.widget.Toast
import java.text.DecimalFormat


class DividendTwoStageMode : AppCompatActivity() {

    lateinit var RecentDividend: EditText
    lateinit var FirstGrowthRate: EditText
    lateinit var FirstGrowthYear: EditText
    lateinit var SecondGrowthRate: EditText
    lateinit var DiscountRate: EditText
    lateinit var set: LinearLayout
    lateinit var Dividend: EditText
    lateinit var SPvOfDividend: EditText
    lateinit var TerminalValue: EditText
    lateinit var PVofTerminal: EditText
    lateinit var PriceOfShare: EditText
    lateinit var heading1: TableRow
    lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dividend_two_stage_mode)

        RecentDividend = findViewById(R.id.RecDiv)
        FirstGrowthRate = findViewById(R.id.FGrthRate)
        FirstGrowthYear = findViewById(R.id.frGrwYr)
        SecondGrowthRate = findViewById(R.id.SecGrwRt)
        DiscountRate = findViewById(R.id.Dsrate)
        set = findViewById(R.id.set)
        Dividend = findViewById(R.id.Divid)
        SPvOfDividend = findViewById(R.id.PVDivi)
        TerminalValue = findViewById(R.id.Terval)
        PVofTerminal = findViewById(R.id.PVofTer)
        PriceOfShare = findViewById(R.id.Share)
        heading1 = findViewById(R.id.table_heading1)
        tableLayout = findViewById(R.id.table)

    }

    fun calculate(view: View) {

        heading1.visibility = View.GONE
        set.visibility = View.GONE
        tableLayout.removeAllViews()
        Dividend.text.clear()
        SPvOfDividend.text.clear()
        TerminalValue.text.clear()
        PVofTerminal.text.clear()
        PriceOfShare.text.clear()


        if (!TextUtils.isEmpty(RecentDividend.text) &&
            !TextUtils.isEmpty(FirstGrowthRate.text) &&
            !TextUtils.isEmpty(FirstGrowthYear.text) &&
            !TextUtils.isEmpty(SecondGrowthRate.text) &&
            !TextUtils.isEmpty(DiscountRate.text)
        ) {

            val RD: Double
            val FG: Double
            val SG: Double
            val DR: Double

            var pvT = 0.0
            var priceOfShare = 0.0

            RD = java.lang.Double.parseDouble(RecentDividend.text.toString())
            FG = java.lang.Double.parseDouble(FirstGrowthRate.text.toString())
            SG = java.lang.Double.parseDouble(SecondGrowthRate.text.toString())
            DR = java.lang.Double.parseDouble(DiscountRate.text.toString())

            val decimalFormat = DecimalFormat("#.###")

            heading1.visibility = View.VISIBLE
            set.visibility = View.VISIBLE

            for (i in 0 until Integer.parseInt(FirstGrowthYear.text.toString()) + 1) {
                val tableRow = layoutInflater.inflate(R.layout.newlrn, null) as TableRow
                tableLayout.addView(tableRow)

                val year = tableRow.getChildAt(0) as EditText
                year.setText(tableLayout.childCount.toString())
                val dividend = tableRow.getChildAt(1) as EditText
                val pvOfDividend = tableRow.getChildAt(2) as EditText

                val d = RD * Math.pow(
                    1 + FG / 100,
                    java.lang.Double.parseDouble(tableLayout.childCount.toString())
                )
                dividend.setText((decimalFormat.format(d)))

                if (i == Integer.parseInt(FirstGrowthYear.text.toString())) {
                    pvOfDividend.setText("-")

                    val terminalvalue = d / (DR / 100 - SG / 100)
                    TerminalValue.setText((decimalFormat.format(terminalvalue)))

                    pvT = terminalvalue / Math.pow(
                        1 + DR / 100,
                        java.lang.Double.parseDouble(tableLayout.childCount.toString()) - 1
                    )
                    priceOfShare += pvT
                    PVofTerminal.setText((decimalFormat.format(pvT)))
                } else {
                    val pv = d / Math.pow(
                        1 + DR / 100,
                        java.lang.Double.parseDouble(tableLayout.childCount.toString())
                    )
                    priceOfShare += pv
                    pvOfDividend.setText((decimalFormat.format(pv)))
                }
            }
            SPvOfDividend.setText((decimalFormat.format(priceOfShare - pvT)))
            PriceOfShare.setText("₹ " + (decimalFormat.format(priceOfShare)))
        } else {
            Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show()
        }

    }
}
