package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import com.example.analytiq.R
import java.text.DecimalFormat

class MeanVarianceSD : AppCompatActivity() {

    lateinit var meanET: EditText
    lateinit var varianceET: EditText
    lateinit var sumET: EditText
    lateinit var SDET: EditText
    lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mean_variance_sd)

        findViewById<ScrollView>(R.id.mean_variance_scroll).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.mean_variance_scroll).isVerticalScrollBarEnabled = false

        tableLayout = findViewById(R.id.table);
        sumET = findViewById(R.id.sum);
        meanET = findViewById(R.id.m);
        varianceET = findViewById(R.id.v);
        SDET = findViewById(R.id.sd);

        for (i in 0 until 4) {
            val tableRow =
                layoutInflater.inflate(R.layout.mean_varience_deviation_rows, null) as TableRow
            tableLayout.addView(tableRow)
        }
    }

    fun calculate(view: View) {

        val decimalFormat = DecimalFormat("#.###");

        var meanSum = 0.0
        var d2Sum = 0.0
        var n = 0


        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow
            val xET = tableRow.getChildAt(0) as EditText

            if (!TextUtils.isEmpty(xET.getText())) {

                n += 1;

                val x = (xET.getText().toString().toDouble())
                meanSum += x;
            }
        }
        val m = meanSum / (n.toString().toDouble())
        meanET.setText((decimalFormat.format(m)).toString())

        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow
            val xET = tableRow.getChildAt(0) as EditText
            val xyET = tableRow.getChildAt(1) as EditText
            val xyzET = tableRow.getChildAt(2) as EditText

            if (!TextUtils.isEmpty(xET.getText())) {

                val x = (xET.getText().toString().toDouble())

                val xy = x - m;
                xyET.setText((decimalFormat.format(xy)).toString())

                val xyz = Math.pow(xy, 2.0)
                xyzET.setText((decimalFormat.format(xyz)).toString())

                d2Sum += xyz;
            }
        }

        sumET.setText((decimalFormat.format(d2Sum)).toString())
        val v = d2Sum / ((n - 1).toString().toDouble())
        varianceET.setText((decimalFormat.format(v)).toString())
        SDET.setText((decimalFormat.format(Math.pow(v, 0.5))).toString())
    }

    fun addRow(view: View) {
        val tableRow =
            layoutInflater.inflate(R.layout.mean_varience_deviation_rows, null) as TableRow
        tableLayout.addView(tableRow)
    }
}

