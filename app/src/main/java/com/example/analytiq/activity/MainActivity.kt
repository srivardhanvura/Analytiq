package com.example.analytiq.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.analytiq.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.logout).setOnClickListener {

            mAuth.signOut()
            finish()
            val intent = Intent(this@MainActivity, Login_Form::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.simpleActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, SimpleInterest::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.compoundActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, CompoundInterest::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.mensurationActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, MensurationActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.cashflowActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, CashFlow::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.depreciationWrittenActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, DepreciationWrittenDown::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.depreciationStraightActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, DepreciationStraightLine::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.npvIrrActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, NPVIRRActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.dep_text_1).setOnClickListener {

            val intent = Intent(this@MainActivity, DepText1::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.dep_text_2).setOnClickListener {

            val intent = Intent(this@MainActivity, DepText2::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.npv_text).setOnClickListener {

            val intent = Intent(this@MainActivity, NPVText::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.normalWithoutActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, NormalDistriActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.normalWithActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, NormalDistriWithMean::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.ytmActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, YTMActivity::class.java)
            startActivity(intent)
        }
    }
}
