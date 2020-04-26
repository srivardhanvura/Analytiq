package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.analytiq.R

class LinearEquation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_equation)

        val x = findViewById<EditText>(R.id.editText)
        val y = findViewById<EditText>(R.id.editText3)
        val z = findViewById<EditText>(R.id.editText5)
        val u = findViewById<EditText>(R.id.editText6)
        val v = findViewById<EditText>(R.id.editText4)
        val w = findViewById<EditText>(R.id.editText7)
        val `as` = findViewById<TextView>(R.id.textView17)
        val ad = findViewById<TextView>(R.id.textView20)
        val submit = findViewById<TextView>(R.id.button)


        submit.setOnClickListener {

            if (u.text.isEmpty() || v.text.isEmpty() || w.text.isEmpty() || x.text.isEmpty() || y.text.isEmpty() || z.text.isEmpty())
                Toast.makeText(this, "Fill properly", Toast.LENGTH_SHORT).show()
            else {
                val s1 = x.getText().toString()
                val s2 = y.getText().toString()
                val s3 = z.getText().toString()
                val s4 = u.getText().toString()
                val s5 = v.getText().toString()
                val s6 = w.getText().toString()
                val a = s1.toDouble()
                val b = s2.toDouble()
                val c = s3.toDouble()
                val d = s4.toDouble()
                val e = s5.toDouble()
                val f = s6.toDouble()
                val det = (a * d) - (b * c)
                val X1 = (d * e) - (b * f)
                val X2 = X1 / det
                `as`.setText("%.3f".format(X2))
                val Y1 = (a * f) - (c * e)
                val Y2 = Y1 / det
                ad.setText("%.3f".format(Y2))

                findViewById<RelativeLayout>(R.id.rel3).visibility = View.VISIBLE
            }
        }
    }
}
