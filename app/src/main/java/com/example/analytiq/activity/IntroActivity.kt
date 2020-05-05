package com.example.analytiq.activity

import android.content.Intent
import android.os.Bundle
import com.example.analytiq.R
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import android.R.attr.description
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import com.github.paolorotolo.appintro.model.SliderPage
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppIntro() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)

        mAuth = FirebaseAuth.getInstance()

        val sharedPref = getSharedPreferences("first", Context.MODE_PRIVATE)
        if (!sharedPref.getBoolean("firstTime", true)) {
            if (mAuth.currentUser != null) {
                startActivity(Intent(getApplicationContext(), MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(getApplicationContext(), Login_Form::class.java))
                finish()
            }
        }

        addSlide(
            AppIntroFragment.newInstance(
                resources.getString(R.string.st),
                resources.getString(R.string.k),
                R.drawable.idea,
                resources.getColor(R.color.first)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                resources.getString(R.string.nd),
                resources.getString(R.string.l),
                R.drawable.money,
                resources.getColor(R.color.second)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                resources.getString(R.string.rd),
                resources.getString(R.string.m),
                R.drawable.dollar,
                resources.getColor(R.color.third)
            )
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
//        if (mAuth.currentUser != null) {
//            startActivity(Intent(applicationContext, MainActivity::class.java))
//            finish()
//        } else {
//            startActivity(Intent(applicationContext, Login_Form::class.java))
//            finish()
//        }
        val sharedPref = getSharedPreferences("first", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("firstTime", false).apply()
        startActivity(Intent(applicationContext, Login_Form::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        val sharedPref = getSharedPreferences("first", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("firstTime", false).apply()
        startActivity(Intent(applicationContext, Login_Form::class.java))
        finish()
    }

}
