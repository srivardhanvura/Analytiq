package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R

class NormalDistriWithMean : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_distri_with_mean)

        val a: EditText = findViewById(R.id.editText)
        val b: EditText = findViewById(R.id.editText2)
        val c: EditText = findViewById(R.id.editText3)
        val submit: Button = findViewById(R.id.button)

        submit.setOnClickListener {
            val s1 = a.text.toString()
            val s2 = b.text.toString()
            val s3 = c.text.toString()
            val mean = Integer.parseInt(s1)
            val sd = Integer.parseInt(s2)
            var x: Double = (Integer.parseInt(s3)).toDouble()
            val Z_MAX = 6.0
            var y: Double
            var z: Double
            val w: Double
            if (mean < x)
                y = x - mean
            else
                y = mean - x
            z = y / sd;
            if (z == 0.0)
                x = 0.0
            else {
                y = 0.5 * z
                if (y > (Z_MAX * 0.5)) {
                    x = 1.0
                } else if (y < 1.0) {
                    w = y * y
                    x = ((((((((0.000124818987 * w - 0.001075204047) * w
                            + 0.005198775019) * w - 0.019198292004) * w
                            + 0.059054035642) * w - 0.151968751364) * w
                            + 0.319152932694) * w - 0.531923007300) * w
                            + 0.797884560593) * y * 2.0;
                } else {
                    y -= 2.0
                    x = (((((((((((((-0.000045255659 * y
                            + 0.000152529290) * y - 0.000019538132) * y
                            - 0.000676904986) * y + 0.001390604284) * y
                            - 0.000794620820) * y - 0.002034254874) * y
                            + 0.006549791214) * y - 0.010557625006) * y
                            + 0.011630447319) * y - 0.009279453341) * y
                            + 0.005353579108) * y - 0.002141268741) * y
                            + 0.000535310849) * y + 0.999936657524;
                }
                if (z > 0.0)
                    z = (x + 1.0) * 0.5
                else
                    z = (1.0 - x) * 0.5
            }
            val s = String.format("%.4f", z)
            z = s.toDouble()
            Toast.makeText(
                this@NormalDistriWithMean,
                "The cumulative z value is " + z + "",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
