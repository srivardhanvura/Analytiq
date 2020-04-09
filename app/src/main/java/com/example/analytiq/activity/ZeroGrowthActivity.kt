package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R

class ZeroGrowthActivity : AppCompatActivity() {

    lateinit var first: EditText
    lateinit var third: EditText
    lateinit var fourth: EditText
    lateinit var calculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zero_growth)

        first = findViewById(R.id.fst) as EditText
        third = findViewById(R.id.thrd) as EditText
        fourth = findViewById(R.id.frth) as EditText
        calculate = findViewById(R.id.calculate) as Button


        calculate.setOnClickListener {

            if (!first.text.isEmpty() && !third.text.isEmpty()) {

                val num1 = java.lang.Double.parseDouble(first.text.toString())
                val num3 = java.lang.Double.parseDouble(third.getText().toString())

                val ke = num3 / 100

                val a = num1 / ke



                fourth.setText(String.format("%.2f", a))
            }else{
                Toast.makeText(this,"Fill properly", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
