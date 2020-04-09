package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R



class DividedBasedActivity : AppCompatActivity() {

    lateinit var first: EditText
    lateinit var second: EditText
    lateinit var third: EditText
    lateinit var fourth: EditText
    lateinit var calculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_divided_based)

        first = findViewById(R.id.fst) as EditText
        second = findViewById(R.id.scnd) as EditText
        third = findViewById(R.id.thrd) as EditText
        fourth = findViewById(R.id.frth) as EditText
        calculate = findViewById(R.id.calculate) as Button


        calculate.setOnClickListener {

            if (!first.text.isEmpty() && !third.text.isEmpty() && !second.text.isEmpty()) {

                val num1 = (first.text.toString()).toDouble()
                val num2 = (second.text.toString()).toDouble()
                val num3 = (third.text.toString()).toDouble()

                val ke = num3 / 100

                val ti = 1 + ke
                val pi = num1 + num2

                val a = pi / ti

                fourth.setText(String.format("%.2f", a))
            }else{
                Toast.makeText(this,"Fill properly",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
