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
                    toolabr.setBackgroundColor(resources.getColor(R.color.pink))
                }

                "YTM" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, YTMOptions()).commit()
                    supportActionBar?.title = "YTM"
                    toolabr.setBackgroundColor(resources.getColor(R.color.light_green))
                }

                "Present Value" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, PresentValueOptions()).commit()
                    supportActionBar?.title = "Present Value"
                    toolabr.setBackgroundColor(resources.getColor(R.color.light_blue))
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
                    toolabr.setBackgroundColor(resources.getColor(R.color.pink))
                }

                "Var Calculation" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, VarOptions()).commit()
                    supportActionBar?.title = "Var Calculation"
                    toolabr.setBackgroundColor(resources.getColor(R.color.light_yellow))
                }

                "Depreciation" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, DepreciationOptions()).commit()
                    supportActionBar?.title = "Depreciation"
                    toolabr.setBackgroundColor(resources.getColor(R.color.dark_yellow))
                }

                "Correlation & Regression" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, CorrelationOptions()).commit()
                    supportActionBar?.title = "Correlation & Regression"
                    toolabr.setBackgroundColor(resources.getColor(R.color.lavender))
                }

                "Mensuration" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, MensurationOptions()).commit()
                    supportActionBar?.title = "Mensuration"
                    toolabr.setBackgroundColor(resources.getColor(R.color.dark_pink))
                }

                "NPV & IRR" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, NPV_IRROptions()).commit()
                    supportActionBar?.title = "NPV & IRR"
                    toolabr.setBackgroundColor(resources.getColor(R.color.gray))
                }
                "Discounting Factor" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, DiscountingFactorOptions()).commit()
                    supportActionBar?.title = "Discounting Factor"
                    toolabr.setBackgroundColor(resources.getColor(R.color.dark_pink))
                }

                "Mean & SD" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, MeanVarianceSDOptions()).commit()
                    supportActionBar?.title = "Mean Variance SD"
                    toolabr.setBackgroundColor(resources.getColor(R.color.yellow))
                }

                "Duration of Bond" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, DurationBondOptions()).commit()
                    supportActionBar?.title = "Duration of Bond"
                    toolabr.setBackgroundColor(resources.getColor(R.color.gray))
                }

                "Normal Distribution" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, NormalDistributionOptions()).commit()
                    supportActionBar?.title = "Normal Distribution"
                    toolabr.setBackgroundColor(resources.getColor(R.color.lavender))
                }

                "EMI" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, EMIOptions()).commit()
                    supportActionBar?.title = "EMI"
                    toolabr.setBackgroundColor(resources.getColor(R.color.light_yellow))
                }

                "Critical Value" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, CriticalValueOptions()).commit()
                    supportActionBar?.title = "Critical Value"
                    toolabr.setBackgroundColor(resources.getColor(R.color.light_green))
                }

                "Currency" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, CurrencyOptions()).commit()
                    supportActionBar?.title = "Currency"
                    toolabr.setBackgroundColor(resources.getColor(R.color.pink))
                }

                "Basic Maths" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, BasicMathsOptions()).commit()
                    supportActionBar?.title = "Basic Maths"
                    toolabr.setBackgroundColor(resources.getColor(R.color.light_blue))
                }

                "Term Structure" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, TermStructureOptions()).commit()
                    supportActionBar?.title = "Term Structure"
                    toolabr.setBackgroundColor(resources.getColor(R.color.yellow))
                }

                "US Share Market" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_2, ShareMarketOptions()).commit()
                    supportActionBar?.title = "Share Market"
                    toolabr.setBackgroundColor(resources.getColor(R.color.light_green))
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
