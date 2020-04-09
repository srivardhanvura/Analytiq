package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.analytiq.R

class DepreciationStraightLine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depreciation_stright_line)

        findViewById<ScrollView>(R.id.straight_line_scroll).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.straight_line_scroll).isVerticalScrollBarEnabled = false

        val a = findViewById<EditText>(R.id.editText)
        val b = findViewById<EditText>(R.id.editText2)
        val c = findViewById<EditText>(R.id.editText3)
        val d = findViewById<EditText>(R.id.editText4)
        val dm = findViewById<TextView>(R.id.textView10)
        val av = findViewById<TextView>(R.id.textView11)
        val submit = findViewById<Button>(R.id.button)
        submit.setOnClickListener {

            if (a.text.isEmpty() || b.text.isEmpty() || c.text.isEmpty() || d.text.isEmpty()) {
                Toast.makeText(this@DepreciationStraightLine, "Fill properly", Toast.LENGTH_SHORT)
                    .show()

            } else {

                val s1 = a.getText().toString()
                val s2 = b.getText().toString()
                val s3 = c.getText().toString()
                val s4 = d.getText().toString()
                val x = Integer.parseInt(s1)
                val y = Integer.parseInt(s2)
                val z = Integer.parseInt(s3)
                val w = Integer.parseInt(s4)
                val u = (x - y) * w
                val v = u / 100
                val U = v * z
                dm.setText(U.toString())
                val V = x - U
                av.setText(V.toString())

                dm.visibility = View.VISIBLE
                av.visibility = View.VISIBLE
            }
        }
    }
}
