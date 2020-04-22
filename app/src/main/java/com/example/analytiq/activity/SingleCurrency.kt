package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.analytiq.R
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.analytiq.fragment.SingleCurrencyHistoricalList
import java.util.*

class SingleCurrency : AppCompatActivity() {

    lateinit var currencyName:String
    lateinit var currencyValue:String
    lateinit var currency:String
    var mMonth=0
    var mDay=0
    var mYear=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_currency)

        val calendar = Calendar.getInstance()
        val currentDate = calendar.getTime()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val intent = intent
        currency = intent.getStringExtra("Currency")
        currencyName = intent.getStringExtra("CurrencyName")
        currencyValue = intent.getStringExtra("CurrencyValue")

        val currencyNameTV:TextView = findViewById(R.id.currency_name)
        val currencyValueTV:TextView = findViewById(R.id.currency_value)
        val currencyTV:TextView = findViewById(R.id.currency)
        currencyNameTV.setText(currencyName)
        currencyTV.setText(currency)
        currencyValueTV.setText(currencyValue)

        val currencyHistoricalVP:ViewPager = findViewById(R.id.currency_historical_VP)

        currencyHistoricalVP.setAdapter(object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when (position) {
                    0 -> return SingleCurrencyHistoricalList(currency, currentDate, 1)
                    1 -> return SingleCurrencyHistoricalList(currency, currentDate, 2)
                    2 -> return SingleCurrencyHistoricalList(currency, currentDate, 3)
                    else->return null as Fragment
                }
            }

            override fun getCount(): Int {
                return 3
            }

            override fun getPageTitle(position: Int): CharSequence? {
                when (position) {
                    0 -> return "Daily"
                    1 -> return "Weekly"
                    2 -> return "Monthly"
                    else -> return null
                }
            }
        })
    }
}
