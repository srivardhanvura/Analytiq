package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import com.example.analytiq.R

class DepText2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dep_text2)

        findViewById<ScrollView>(R.id.dep_text2_scroll).isHorizontalScrollBarEnabled=false
        findViewById<ScrollView>(R.id.dep_text2_scroll).isVerticalScrollBarEnabled=false
    }
}
