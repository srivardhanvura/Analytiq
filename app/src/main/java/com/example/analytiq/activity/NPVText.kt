package com.example.analytiq.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import com.example.analytiq.R

class NPVText : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_npvtext)

        findViewById<ScrollView>(R.id.npv_text_scroll).isHorizontalScrollBarEnabled=false
        findViewById<ScrollView>(R.id.npv_text_scroll).isVerticalScrollBarEnabled=false


        findViewById<Button>(R.id.btn).setOnClickListener {
            val intent= Intent(this@NPVText,NPVIRRActivity::class.java)
            startActivity(intent)
        }
    }
}
