package com.example.analytiq.activity

import android.content.Intent
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import com.example.analytiq.R

class DepText1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dep_text1)

        findViewById<ScrollView>(R.id.dep_text1_scroll).isHorizontalScrollBarEnabled=false
        findViewById<ScrollView>(R.id.dep_text1_scroll).isVerticalScrollBarEnabled=false

        findViewById<Button>(R.id.btn).setOnClickListener {
            val intent= Intent(this@DepText1,DepreciationStraightLine::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            val intent= Intent(this@DepText1,DepreciationWrittenDown::class.java)
            startActivity(intent)
        }
    }
}
