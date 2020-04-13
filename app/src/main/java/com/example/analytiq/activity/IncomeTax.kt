package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.analytiq.R

class IncomeTax : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_tax)

        findViewById<ScrollView>(R.id.income_tax_scroll).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.income_tax_scroll).isVerticalScrollBarEnabled = false

        val a = findViewById<EditText>(R.id.editText)
        val b = findViewById<EditText>(R.id.editText2)
        val c = findViewById<EditText>(R.id.editText3)
        val submit = findViewById<Button>(R.id.button)
        val d = findViewById<TextView>(R.id.textView5)
        val e = findViewById<TextView>(R.id.textView7)
        submit.setOnClickListener {

            if (a.text.isEmpty() || b.text.isEmpty() || c.text.isEmpty()) {
                Toast.makeText(this, "Fill prpoperly", Toast.LENGTH_SHORT).show()
            } else {

                val s1 = a.getText().toString().toDouble()
                val s2 = b.getText().toString().toDouble()
                val s3 = c.getText().toString().toDouble()
                val x = s1
                val y = s2
                val z = s3
                val u = x + y - z
                d.setText("Annual Income(in rupees): " + u.toString())
                var income_tax = 0.0
                if (u < 250000) {
                    e.setText("Tax(in rupees): " + income_tax.toString())
                } else if (u >= 250000 && u < 500000) {
                    income_tax = 5 * u / 100
                    e.setText("Tax(in rupees): " + income_tax.toString())
                } else if (u >= 500000 && u < 750000) {
                    income_tax = 10 * u / 100
                    e.setText("Tax(in rupees): " + income_tax.toString())
                } else if (u >= 750000 && u < 1000000) {
                    income_tax = 15 * u / 100
                    e.setText("Tax(in rupees): " + income_tax.toString())
                } else if (u >= 1000000 && u < 1250000) {
                    income_tax = 20 * u / 100
                    e.setText("Tax(in rupees): " + income_tax.toString())
                } else if (u >= 1250000 && u < 1500000) {
                    income_tax = 25 * u / 100
                    e.setText("Tax(in rupees): " + income_tax.toString())
                } else if (u >= 1500000) {
                    income_tax = 30 * u / 100
                    e.setText("Tax(in rupees): " + income_tax.toString())
                }
            }

            d.visibility= View.VISIBLE
            e.visibility=View.VISIBLE
        }
    }
}
