package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.analytiq.R

class EMICalculation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emicalculation)

        findViewById<ScrollView>(R.id.emi_scroll).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.emi_scroll).isVerticalScrollBarEnabled = false


        val a: EditText = findViewById(R.id.editText2)
        val b: EditText = findViewById(R.id.editText3)
        val c: EditText = findViewById(R.id.editText4)
        val `as`: TextView = findViewById(R.id.textView5)
        val ad: TextView = findViewById(R.id.textView4)
        val af: TextView = findViewById(R.id.textView9)
        val calculate: Button = findViewById(R.id.button)
        calculate.setOnClickListener {

            if (a.text.isEmpty() || b.text.isEmpty() || c.text.isEmpty()) {
                Toast.makeText(this, "Fill properly", Toast.LENGTH_SHORT).show()
            } else {
                val s1 = a.getText().toString().toDouble()
                val s2 = b.getText().toString().toDouble()
                val s3 = c.getText().toString().toDouble()
                val p = s1
                val R = s2
                val N = s3
                val r = R / 1200
                val n = N * 12
                val emi1 = Math.pow(1 + r, n)
                val emi = p * r * emi1
                val emi2 = emi / (emi1 - 1)
                val s = java.lang.Double.toString(emi2)
                val total_amount = emi2 * n
                val Interest = total_amount - p
                ad.setText("Interest: " +"%.4f".format( Interest))
                af.setText("Total Amount(in rupees)" + "%.4f".format(total_amount))
                `as`.setText("EMI(in rupees)" + "%.4f".format(emi2))

                ad.visibility = View.VISIBLE
                af.visibility = View.VISIBLE
                `as`.visibility = View.VISIBLE
            }

        }
    }
}
