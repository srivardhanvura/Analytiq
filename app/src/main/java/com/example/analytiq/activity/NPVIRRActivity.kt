package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.analytiq.R
import java.text.DecimalFormat

class NPVIRRActivity : AppCompatActivity() {

    lateinit var tableLayout: TableLayout
    lateinit var irr: EditText
    lateinit var npv: EditText
    lateinit var rate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_npvirr)

        findViewById<ScrollView>(R.id.npr_ivv_scroll).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.npr_ivv_scroll).isVerticalScrollBarEnabled = false

        tableLayout = findViewById(R.id.table)
        rate = findViewById(R.id.coc)
        npv = findViewById(R.id.npv)
        irr = findViewById(R.id.irr)

        for (i in 0 until 4) {
            val tableRow = getLayoutInflater().inflate(R.layout.npv_irr_rows, null) as TableRow;
            tableLayout.addView(tableRow);
//            TextView sno = (TextView) tableRow.getChildAt(0);
//            sno.setText(String.valueOf(tableLayout.getChildCount()));

            if (i == 0) {
                val negamount: EditText = tableRow.getChildAt(0) as EditText

                negamount.setText("- ")
                Selection.setSelection(negamount.getText(), negamount.getText().length)


                negamount.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (!s.toString().startsWith("- ")) {
                            negamount.setText("- ");
                            Selection.setSelection(
                                negamount.getText(),
                                negamount.getText().length
                            );
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }
                })

                val time: EditText = tableRow.getChildAt(1) as EditText
                time.setText("0");
                time.setEnabled(false);

//                tableRow.getChildAt(2).setVisibility(View.INVISIBLE);
            }
        }
    }

    fun calculate(view: View) {
        val decimalFormat = DecimalFormat("#.###")

        var amounts = ArrayList<String>()
        var time = ArrayList<String>()

        var totalPresentValue = 0.0

        var tableRow1 = tableLayout.getChildAt(0) as TableRow
        var amount1 = tableRow1.getChildAt(0) as EditText

        if (!TextUtils.isEmpty(amount1.getText().toString().substring(2)) && !(TextUtils.isEmpty(
                rate.getText().toString()
            ))
        ) {
            var r = (rate.getText().toString()).toDouble();
            var A = (amount1.getText().toString().substring(2)).toDouble();

            for (i in 1 until tableLayout.childCount) {
                var tableRow = tableLayout.getChildAt(i) as TableRow
                var amount = tableRow.getChildAt(0) as EditText
                var timeInterval = tableRow.getChildAt(1) as EditText
                var presentValue = tableRow.getChildAt(2) as EditText

                var pv: Double;
                var a: Double;
                var t: Double;

                if (!TextUtils.isEmpty(amount.getText()) && !TextUtils.isEmpty(timeInterval.getText())) {
                    amounts.add(amount.getText().toString());
                    time.add(timeInterval.getText().toString());
                    a = amount.text.toString().toDouble();
                    t = timeInterval.text.toString().toDouble();

                    pv = a / Math.pow(1 + (r / 100), t);

                    totalPresentValue += pv;

                    presentValue.setText((decimalFormat.format(pv)).toString());

                } else {
                    if (!TextUtils.isEmpty(amount.getText()) || !TextUtils.isEmpty(timeInterval.getText())) {
                        Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                npv.setText(decimalFormat.format(totalPresentValue - A));
            }

            var iRR = IRR(A, amounts, time);

            if (iRR == 300.0) {
                irr.setText(">100");
            } else if (iRR == 200.0) {
                irr.setText("<0");
            } else {
                irr.setText((iRR).toString());
            }
        } else {
            Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show();
        }
    }

    fun addRow(view: View) {
        var tableRow = getLayoutInflater().inflate(R.layout.npv_irr_rows, null);
        tableLayout.addView(tableRow);
    }

    fun IRR(A: Double, amounts: List<String>, time: List<String>): Double {

        val MAX_LIMIT = 100000;
        var irr = 300.0;

        for (r in 0 until MAX_LIMIT + 1) {

            var fr: Double = 0.0;
            for (i in 0 until amounts.size) {
                var fri = (amounts.get(i).toDouble()) / Math.pow(
                    1 + (r / ("100000").toDouble()).toDouble(),
                    (time.get(i)).toDouble()
                );
                fr += fri;
            }

            if (fr == A) {
                irr = r / ("1000").toDouble();
                break;
            } else if (fr < A) {

                if (r == 0) {
                    if (A - fr < ("0.001").toDouble()) {
                        irr = (r) / ("1000".toDouble());
                    } else {
                        irr = 200.0;
                    }
                } else {
                    irr = (r) / ("1000".toDouble());
                }
                break;
            }
        }

        if (irr == 200.0) {
            return 200.0;
        } else {
            return irr;
        }
    }
}
