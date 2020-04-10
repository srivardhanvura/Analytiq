package com.example.analytiq.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.analytiq.R
import com.google.firebase.auth.FirebaseAuth
import java.security.Permission

class MainActivity : AppCompatActivity() {

    val STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestStoragePermissions()
        }

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

        findViewById<Button>(R.id.corrActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, CorrelationActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.varActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, VarActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.discountingActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, DiscountingFactor::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.zerogrowthActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, ZeroGrowthActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.constantgrowthActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, ConstantGrowthActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.dividedbasedthActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, DividedBasedActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.durationInYearsActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, DurationInYears::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.meanVarianceActivity).setOnClickListener {

            val intent = Intent(this@MainActivity, MeanVarianceSD::class.java)
            startActivity(intent)
        }
    }

    fun requestStoragePermissions() {
        val permi = Array<String>(1,{i->""})
        permi[0]=Manifest.permission.READ_EXTERNAL_STORAGE
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Storage permission required")
            alert.setMessage("Storage permission required to import excel files")
            alert.setPositiveButton("Ok") { text, listener ->
                ActivityCompat.requestPermissions(this@MainActivity, permi, STORAGE_PERMISSION)
            }
            alert.setNegativeButton("Cancel") { text, listener -> }
            alert.create()
            alert.show()
        } else {
            ActivityCompat.requestPermissions(this, permi, STORAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == STORAGE_PERMISSION) {
            if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
