package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.analytiq.R


class DepreciationWrittenDown : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depreciation_written_down)

        findViewById<ScrollView>(R.id.written_scroll_view).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.written_scroll_view).isVerticalScrollBarEnabled = false

        val a = findViewById<EditText>(R.id.editText)
        val b = findViewById<EditText>(R.id.editText2)
        val c = findViewById<EditText>(R.id.editText3)
        val d = findViewById<EditText>(R.id.editText4)
        val dm = findViewById<TextView>(R.id.textView10)
        val av = findViewById<TextView>(R.id.textView11)

        val submit = findViewById<Button>(R.id.button)
        submit.setOnClickListener {
            if (a.text.isEmpty() || b.text.isEmpty() || c.text.isEmpty() || d.text.isEmpty()) {
                Toast.makeText(this@DepreciationWrittenDown, "Fill properly", Toast.LENGTH_SHORT)
                    .show()

            } else {

                val s1 = a.getText().toString()
                val s2 = b.getText().toString()
                val s3 = c.getText().toString()
                val s4 = d.getText().toString()
                val x = (s1).toDouble()
                val y = (s2).toDouble()
                val z = (s3).toDouble()
                val w = (s4).toDouble()
                var i: Int
                var U = 0.0
                var dep_amount: Double
                var amount: Double
                val total_amount: Double
                amount = (x - y)
                i = 0
                while (i < z) {
                    dep_amount = amount * w / 100.0
                    U = U + dep_amount
                    amount = amount - dep_amount
                    i++
                }
                total_amount = x - U
                dm.setText("%.3f".format(U))
                av.setText("%.3f".format(total_amount))

                findViewById<LinearLayout>(R.id.linear1).visibility=View.VISIBLE
                findViewById<LinearLayout>(R.id.linear2).visibility=View.VISIBLE

                dm.visibility = View.VISIBLE
                av.visibility = View.VISIBLE
            }
        }
    }
}
