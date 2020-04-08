package com.example.analytiq.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.analytiq.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_page_3)

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser


        findViewById<Button>(R.id.previous_3).setOnClickListener {
            val intent = Intent(this@SplashActivity3, SplashActivity2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
        }

        findViewById<Button>(R.id.next_3).setOnClickListener {
            if (user != null && user.isEmailVerified) {
                val intent = Intent(this@SplashActivity3, MainActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
            } else {
                val login = Intent(this@SplashActivity3, Login_Form::class.java)
                startActivity(login)
                finish()
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@SplashActivity3, SplashActivity2::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}
