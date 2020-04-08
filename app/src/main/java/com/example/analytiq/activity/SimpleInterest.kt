package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import com.example.analytiq.R


class SimpleInterest : AppCompatActivity() {

//    lateinit var PA:TextView
//    lateinit var Interest_Rate:TextView
//    lateinit var Years:TextView
    lateinit var PA_bar:EditText
    lateinit var IR_bar:EditText
    lateinit var year_bar:EditText
    lateinit var simplee:EditText
    lateinit var calculate:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_interest)

        findViewById<ScrollView>(R.id.simple_scroll).isHorizontalScrollBarEnabled=false
        findViewById<ScrollView>(R.id.simple_scroll).isVerticalScrollBarEnabled=false

//        PA = findViewById(R.id.Princ)
//        Interest_Rate = findViewById(R.id.Interest)
//        Years = findViewById(R.id.Yrs)
        PA_bar = findViewById(R.id.Princc)
        IR_bar = findViewById(R.id.Interestt)
        year_bar = findViewById(R.id.Yrss)
        calculate = findViewById(R.id.calculate)
        simplee = findViewById(R.id.simplee)

        calculate.setOnClickListener {

                val num1 = Integer.parseInt(PA_bar.text.toString())
                val num2 = Integer.parseInt(IR_bar.text.toString())
                val num = Integer.parseInt(year_bar.text.toString())
                val si = num1 * num2 * num / 100
                simplee.setText(Integer.toString(si))
        }
    }
}
