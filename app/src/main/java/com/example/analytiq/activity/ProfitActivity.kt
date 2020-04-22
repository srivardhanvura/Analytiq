package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R

class ProfitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profit)

        val first = findViewById<EditText>(R.id.firstEt)
        val second = findViewById<EditText>(R.id.secondEt)
        val third = findViewById<EditText>(R.id.thirdEt)
        val fourth = findViewById<EditText>(R.id.fourthEt)
        val calculate = findViewById<Button>(R.id.calculate)

        calculate.setOnClickListener {
            var c = 0
            if (first.text.isEmpty())
                c++
            if (second.text.isEmpty())
                c++
            if (third.text.isEmpty())
                c++
            if (fourth.text.isEmpty())
                c++

            if (c > 2)
                Toast.makeText(this, "Fill any 2 fields", Toast.LENGTH_SHORT).show()
            else if (c < 2)
                Toast.makeText(this, "Fill only 2 fields", Toast.LENGTH_SHORT).show()
            else {
                if (first.text.isNotEmpty() && second.text.isNotEmpty()) {
                    val f = first.text.toString().toDouble()
                    val s = second.text.toString().toDouble()
                    val t = f / (1 - (s / 100))
                    third.setText("%.3f".format(t))
                    val fo = t - f
                    fourth.setText("%.3f".format(fo))
                } else if (first.text.isNotEmpty() && third.text.isNotEmpty()) {
                    val f = first.text.toString().toDouble()
                    val t = third.text.toString().toDouble()
                    val fo = t - f
                    val s = (fo / t) * 100
                    second.setText("%.3f".format(s))
                    fourth.setText("%.3f".format(fo))
                } else if (first.text.isNotEmpty() && fourth.text.isNotEmpty()) {
                    val f = first.text.toString().toDouble()
                    val fo = fourth.text.toString().toDouble()
                    val t = f + fo
                    val s = (fo / t) * 100
                    third.setText("%.3f".format(t))
                    second.setText("%.3f".format(s))
                } else if (second.text.isNotEmpty() && third.text.isNotEmpty()) {
                    val s = second.text.toString().toDouble()
                    val t = third.text.toString().toDouble()
                    val f = (1 - (s / 100)) * t
                    val fo = t - f
                    first.setText("%.3f".format(f))
                    fourth.setText("%.3f".format(fo))
                } else if (second.text.isNotEmpty() && fourth.text.isNotEmpty()) {
                    val s = second.text.toString().toDouble()
                    val fo = fourth.text.toString().toDouble()
                    val t = fo / (s / 100)
                    val f = t - fo
                    third.setText("%.3f".format(t))
                    fourth.setText("%.3f".format(fo))
                } else if (third.text.isNotEmpty() && fourth.text.isNotEmpty()) {
                    val t = third.text.toString().toDouble()
                    val fo = fourth.text.toString().toDouble()
                    val f = t - fo
                    val s = (fo / t) * 100
                    first.setText("%.3f".format(f))
                    second.setText("%.3f".format(s))
                }
                if(second.text.toString().toDouble()>100)
                    Toast.makeText(this,"Margin cannot be greater than 100. Please input lower value",Toast.LENGTH_LONG ).show()
            }
        }
    }
}
