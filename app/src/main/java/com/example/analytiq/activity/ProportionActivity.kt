package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R

class ProportionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proportion)

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

            if (c == 4)
                Toast.makeText(this, "Fill atleast one field", Toast.LENGTH_SHORT).show()
            else if (c == 1) {
                if (first.text.isEmpty()) {
                    val s = second.text.toString().toDouble()
                    val t = third.text.toString().toDouble()
                    val fo = fourth.text.toString().toDouble()

                    val f = (t / fo) * s
                    first.setText("%.3f".format(f))
                } else if (second.text.isEmpty()) {
                    val f = first.text.toString().toDouble()
                    val t = third.text.toString().toDouble()
                    val fo = fourth.text.toString().toDouble()

                    val s = (f * fo) / t
                    second.setText("%.3f".format(s))
                } else if (third.text.isEmpty()) {
                    val f = first.text.toString().toDouble()
                    val s = second.text.toString().toDouble()
                    val fo = fourth.text.toString().toDouble()

                    val t = (f / s) * fo
                    third.setText("%.3f".format(t))
                } else {
                    val f = first.text.toString().toDouble()
                    val t = third.text.toString().toDouble()
                    val s = second.text.toString().toDouble()

                    val fo = (t * s) / f
                    fourth.setText("%.3f".format(fo))
                }
            } else if (c == 2) {
                if (first.text.isNotEmpty() && second.text.isNotEmpty()) {
                    third.setText(first.text.toString())
                    fourth.setText(second.text.toString())
                } else if (first.text.isNotEmpty() && third.text.isNotEmpty()) {
                    second.setText("1.000")
                    val fo = third.text.toString().toDouble() / first.text.toString().toDouble()
                    fourth.setText("%.3f".format(fo))
                } else if (first.text.isNotEmpty() && fourth.text.isNotEmpty()) {
                    second.setText("1.000")
                    val t = first.text.toString().toDouble() * fourth.text.toString().toDouble()
                    third.setText("%.3f".format(t))
                } else if (second.text.isNotEmpty() && third.text.isNotEmpty()) {
                    fourth.setText("1.000")
                    val f = third.text.toString().toDouble() * second.text.toString().toDouble()
                    first.setText("%.3f".format(f))
                } else if (second.text.isNotEmpty() && fourth.text.isNotEmpty()) {
                    third.setText("1.000")
                    val f = second.text.toString().toDouble() / fourth.text.toString().toDouble()
                    first.setText("%.3f".format(f))
                } else if(third.text.isNotEmpty()&&fourth.text.isNotEmpty()){
                    second.setText("1.000")
                    val f = third.text.toString().toDouble() / fourth.text.toString().toDouble()
                    first.setText("%.3f".format(f))
                }
            }else{
                if(first.text.isNotEmpty()){
                    third.setText(first.text.toString())
                    second.setText("1.000")
                    fourth.setText("1.000")
                }else if(second.text.isNotEmpty()){
                    fourth.setText(second.text.toString())
                    third.setText("1.000")
                    first.setText("1.000")
                }else if(third.text.isNotEmpty()){
                    first.setText(third.text.toString())
                    second.setText("1.000")
                    fourth.setText("1.000")
                }else{
                    second.setText(fourth.text.toString())
                    third.setText("1.000")
                    first.setText("1.000")
                }
            }
        }
    }
}
