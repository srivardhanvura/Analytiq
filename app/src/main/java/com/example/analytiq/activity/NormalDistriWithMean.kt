package com.example.analytiq.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.analytiq.R
import androidx.appcompat.app.AppCompatActivity

class NormalDistriWithMean : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_distri_with_mean)

        val a = findViewById<EditText>(R.id.editText)
        val b = findViewById<EditText>(R.id.editText2)
        val c = findViewById<EditText>(R.id.editText3)
        val submit = findViewById<Button>(R.id.button)
        submit.setOnClickListener {

            if (a.text.isEmpty() || b.text.isEmpty() || c.text.isEmpty()) {
                Toast.makeText(this, "Fill properly", Toast.LENGTH_SHORT).show()
            } else {

                val s1 = a.getText().toString().toDouble()
                val s2 = b.getText().toString().toDouble()
                val s3 = c.getText().toString().toDouble()
                val mean =s1
                val sd = s2
                var x = s3
                val Z_MAX = 6.0
                var y: Double
                var z: Double
                val w: Double
                if (mean < x)
                    y = x - mean
                else
                    y = mean - x
                z = y / sd
                if (z == 0.0)
                    x = 0.0
                else {
                    y = 0.5 * z
                    if (y > Z_MAX * 0.5) {
                        x = 1.0
                    } else if (y < 1.0) {
                        w = y * y
                        x =
                            ((((((((0.000124818987 * w - 0.001075204047) * w + 0.005198775019) * w - 0.019198292004) * w + 0.059054035642) * w - 0.151968751364) * w + 0.319152932694) * w - 0.531923007300) * w + 0.797884560593) * y * 2.0
                    } else {
                        y -= 2.0
                        x =
                            (((((((((((((-0.000045255659 * y + 0.000152529290) * y - 0.000019538132) * y - 0.000676904986) * y + 0.001390604284) * y - 0.000794620820) * y - 0.002034254874) * y + 0.006549791214) * y - 0.010557625006) * y + 0.011630447319) * y - 0.009279453341) * y + 0.005353579108) * y - 0.002141268741) * y + 0.000535310849) * y + 0.999936657524
                    }
                    if (z > 0.0)
                        z = (x + 1.0) * 0.5
                    else
                        z = (1.0 - x) * 0.5
                }
                val s = String.format("%.4f", z)
                z = java.lang.Double.parseDouble(s)
                findViewById<TextView>(R.id.resTxt).setText("The cumulative z value is $z")
            }
        }
    }
}