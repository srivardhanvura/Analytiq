package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R

class ConstantGrowthActivity : AppCompatActivity() {

    lateinit var first: EditText
    lateinit var second: EditText
    lateinit var third: EditText
    lateinit var fourth: EditText
    lateinit var calculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constant_growth)

        first = findViewById(R.id.fst)
        second = findViewById(R.id.scnd)
        third = findViewById(R.id.thrd)
        fourth = findViewById(R.id.frth)
        calculate = findViewById(R.id.calculate)


        calculate.setOnClickListener {

            if (!first.text.isEmpty() && !third.text.isEmpty() && !second.text.isEmpty()) {

                val num1 = (first.text.toString()).toDouble()
                val num2 = (second.text.toString()).toDouble()
                val num3 = (third.text.toString()).toDouble()

                val g = num2 / 100
                val ke = num3 / 100

                val f = num1 * 1
                val s = num1 * g
                val ti = f + s
                val pi = ke - g
                val a = ti / pi

                fourth.setText(String.format("%.2f", a))
            } else {
                Toast.makeText(this, "Fill properly", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
