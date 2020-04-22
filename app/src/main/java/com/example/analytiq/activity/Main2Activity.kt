package com.example.analytiq.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.analytiq.R
import com.example.analytiq.fragment.*

class Main2Activity : AppCompatActivity() {

    lateinit var toolabr: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        toolabr = findViewById(R.id.toolbar_2)
        setSupportActionBar(toolabr)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var name: String? = null

        if (intent != null)
            name = intent.getStringExtra("name")

        if (name != null) {
            when (name) {
                "Interest" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, InterestOptions()).commit()
                    supportActionBar?.title = "Interest"
                }

                "YTM" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, YTMOptions()).commit()
                    supportActionBar?.title = "YTM"
                }

                "Present Value" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, PresentValueOptions()).commit()
                    supportActionBar?.title = "Present Value"
                }

                "Bond Valuation" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, BondValuationOptions()).commit()
                    supportActionBar?.title = "Bond Valuation"
                }

                "Equity Pricing" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, EquityPricingOptions()).commit()
                    supportActionBar?.title = "Equity Pricing"
                }

                "Var Calculation" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, VarOptions()).commit()
                    supportActionBar?.title = "Var Calculation"
                }

                "Depreciation" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, DepreciationOptions()).commit()
                    supportActionBar?.title = "Depreciation"
                }

                "Correlation & Regression" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, CorrelationOptions()).commit()
                    supportActionBar?.title = "Correlation & Regression"
                }

                "Mensuration" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, MensurationOptions()).commit()
                    supportActionBar?.title = "Mensuration"
                }

                "NPV & IRR" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, NPV_IRROptions()).commit()
                    supportActionBar?.title = "NPV & IRR"
                }

                "Power of Number" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, FactorialPowerOptions()).commit()
                    supportActionBar?.title = "Power of Number"
                }

                "Discounting Factor" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, DiscountingFactorOptions()).commit()
                    supportActionBar?.title = "Discounting Factor"
                }

                "Mean & SD" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, MeanVarianceSDOptions()).commit()
                    supportActionBar?.title = "Mean Variance SD"
                }

                "Duration of Bond" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, DurationBondOptions()).commit()
                    supportActionBar?.title = "Duration of Bond"
                }

                "Normal Distribution" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, NormalDistributionOptions()).commit()
                    supportActionBar?.title = "Normal Distribution"
                }

                "EMI"->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, EMIOptions()).commit()
                    supportActionBar?.title = "EMI"
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
    }
}
