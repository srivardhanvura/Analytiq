package com.example.analytiq.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.analytiq.R

class SplashActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_page_2)

        findViewById<Button>(R.id.next_2).setOnClickListener {
            val intent= Intent(this@SplashActivity2,SplashActivity3::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        }

        findViewById<Button>(R.id.previous_2).setOnClickListener {
            val intent= Intent(this@SplashActivity2,SplashActivity1::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
        }
    }

    override fun onBackPressed() {
        val intent= Intent(this@SplashActivity2,SplashActivity1::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}
