package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.widget.Button
import com.example.analytiq.R
import com.google.firebase.auth.FirebaseAuth


class SplashActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_page_1)

        findViewById<Button>(R.id.next_1).setOnClickListener {

            val intent=Intent(this@SplashActivity1,SplashActivity2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        }

    }
}
