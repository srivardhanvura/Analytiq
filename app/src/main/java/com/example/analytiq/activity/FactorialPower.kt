package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.analytiq.R

class FactorialPower : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factorial_power)

        findViewById<ScrollView>(R.id.factorial_power_scroll).isHorizontalScrollBarEnabled=false
        findViewById<ScrollView>(R.id.factorial_power_scroll).isVerticalScrollBarEnabled=false

        val a:EditText = findViewById(R.id.editText)
        val b:EditText = findViewById(R.id.editText2)
        val power = findViewById<TextView>(R.id.textView8)
        val submit = findViewById<Button>(R.id.button)
        submit.setOnClickListener {
            if (a.text.isEmpty() && b.text.isEmpty()) {
                Toast.makeText(this, "Fill properly", Toast.LENGTH_SHORT).show()
            } else {
                val s1 = a.getText().toString().toDouble()
                val s2 = b.getText().toString().toDouble()
                val x = s1
                val y = s2
                val z = 1.0 / y
                val c = Math.pow(x, z)
                power.setText("Answer: "+ "%.4f".format(c))
                power.visibility = View.VISIBLE
            }
        }
    }
}
