package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.analytiq.R
import com.example.analytiq.model.Company
import com.example.analytiq.util.ConnectivityManager
import org.json.JSONException
import com.example.analytiq.model.JSONUtils
import org.json.JSONArray
import org.json.JSONObject
import android.content.Context
import android.graphics.Color
import androidx.loader.content.AsyncTaskLoader
import com.example.analytiq.model.Constants
import java.io.IOException
import java.text.DecimalFormat

class SingleCompany : AppCompatActivity(), LoaderManager.LoaderCallbacks<Company> {

    lateinit var mSymbol: String
    lateinit var mCompanyName: String
    lateinit var exchange: TextView
    lateinit var website: TextView
    lateinit var industry: TextView
    lateinit var description: TextView
    lateinit var ceo: TextView
    lateinit var sector: TextView
    lateinit var price: TextView
    lateinit var change: TextView
    lateinit var changesPercentage: TextView
    lateinit var dayLow: TextView
    lateinit var dayHigh: TextView
    lateinit var yearLow: TextView
    lateinit var yearHigh: TextView
    lateinit var volume: TextView
    lateinit var avgVolume: TextView
    lateinit var open: TextView
    lateinit var previousClose: TextView
    lateinit var noData: TextView
    lateinit var progressBar: ProgressBar
    lateinit var profileCV: CardView
    lateinit var dataCV: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_company)

        val intent = intent
        mSymbol = intent.getStringExtra("Symbol")
        mCompanyName = intent.getStringExtra("CompanyName")
        val symbol = findViewById<TextView>(R.id.symbol)
        symbol.setText(mSymbol)
        val companyName = findViewById<TextView>(R.id.company_name)
        companyName.setText(mCompanyName)

        exchange = findViewById(R.id.exchange)
        industry = findViewById(R.id.industry)
        website = findViewById(R.id.website)
        description = findViewById(R.id.description)
        ceo = findViewById(R.id.ceo)
        sector = findViewById(R.id.sector)
        price = findViewById(R.id.price)
        changesPercentage = findViewById(R.id.change_percent)
        change = findViewById(R.id.change)
        dayLow = findViewById(R.id.day_low)
        dayHigh = findViewById(R.id.day_high)
        yearHigh = findViewById(R.id.year_high)
        yearLow = findViewById(R.id.year_low)
        volume = findViewById(R.id.volume)
        avgVolume = findViewById(R.id.volume_avg)
        open = findViewById(R.id.open)
        previousClose = findViewById(R.id.previous_close)
        noData = findViewById(R.id.no_data)
        progressBar = findViewById(R.id.progressbar)
        profileCV = findViewById(R.id.profile_cardview)
        dataCV = findViewById(R.id.data_cardview)

        if (ConnectivityManager().checkConnectivity(this)) {
            noData.text = null
            progressBar.visibility = View.VISIBLE
            profileCV.visibility = View.GONE
            dataCV.visibility = View.GONE
            supportLoaderManager.initLoader(1, null, this)
        } else {
            noData.text = "No internet Connection"
            progressBar.visibility = View.GONE
            profileCV.visibility = View.GONE
            dataCV.visibility = View.GONE
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Company> {
        return CompanyLoader(this, mSymbol)
    }

    override fun onLoadFinished(loader: Loader<Company>, data: Company?) {
        if (data != null) {
            noData.text = null
            progressBar.visibility = View.GONE
            profileCV.visibility = View.VISIBLE
            dataCV.visibility = View.VISIBLE
            val decimalFormat = DecimalFormat("#.###")

            if (data.getExchange() != null && !data.getExchange().equals("null")) {
                exchange.text = data.getExchange()
            }
            if (data.getIndustry() != null && !data.getIndustry().equals("null")) {
                industry.text = data.getIndustry()
            }
            if (data.getWebsite() != null && !data.getWebsite().equals("null")) {
                website.text = data.getWebsite()
            }
            if (data.getDescription() != null && !data.getDescription().equals("null")) {
                description.text = data.getDescription()
            }
            if (data.getCeo() != null && !data.getCeo().equals("null")) {
                ceo.text = data.getCeo()
            }
            if (data.getSector() != null && !data.getSector().equals("null")) {
                sector.text = data.getSector()
            }
            if (data.getPrice() != null) {
                price.setText(decimalFormat.format(data.getPrice()))
            }
            if (data.getDayLow() != null) {
                dayLow.setText(decimalFormat.format(data.getDayLow()))
            }
            if (data.getDayHigh() != null) {
                dayHigh.setText(decimalFormat.format(data.getDayHigh()))
            }
            if (data.getYearHigh() != null) {
                yearHigh.setText(decimalFormat.format(data.getYearHigh()))
            }
            if (data.getYearLow() != null) {
                yearLow.setText(decimalFormat.format(data.getYearLow()))
            }
            if (data.getdVolume() != null) {
                volume.setText(decimalFormat.format(data.getdVolume()))
            }
            if (data.getAvgVolume() != null) {
                avgVolume.setText(decimalFormat.format(data.getAvgVolume()))
            }
            if (data.getdOpen() != null) {
                open.setText(decimalFormat.format(data.getdOpen()))
            }
            if (data.getdPreviousClose() != null) {
                previousClose.setText(decimalFormat.format(data.getdPreviousClose()))
            }
            if (data.getChange() != null && data.getChangesPercentage() != null) {
                if (data.getChange() >= 0) {
                    changesPercentage.setTextColor(Color.parseColor("#00C853"))
                    change.setTextColor(Color.parseColor("#00C853"))

                    if (data.getChange().toString().startsWith("+")) {
                        change.setText(decimalFormat.format(data.getChange()))
                    } else {
                        change.text = "+" + decimalFormat.format(data.getChange())
                    }

                    if (data.getChangesPercentage().toString().startsWith("+")) {
                        changesPercentage.setText(decimalFormat.format(data.getChangesPercentage()) + "%")
                    } else {
                        changesPercentage.text =
                            "+" + decimalFormat.format(data.getChangesPercentage()) + "%"
                    }

                } else {
                    changesPercentage.setTextColor(Color.parseColor("#FF3D00"))
                    change.setTextColor(Color.parseColor("#FF3D00"))
                    changesPercentage.setText(decimalFormat.format(data.getChangesPercentage()) + "%")
                    change.setText(decimalFormat.format(data.getChange()))
                }
            }
        } else {
            noData.text = "Currently no data is available"
            progressBar.visibility = View.GONE
            profileCV.visibility = View.GONE
            dataCV.visibility = View.GONE
        }
    }

    override fun onLoaderReset(loader: Loader<Company>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private class CompanyLoader(context: Context, internal var mSymbol: String) :
        AsyncTaskLoader<Company>(context) {

        override fun onStartLoading() {
            super.onStartLoading()
            forceLoad()
        }

        override fun loadInBackground(): Company? {
            return fetchCompanyData(mSymbol)
        }

        private fun fetchCompanyData(symbol: String): Company? {

            var profile: JSONObject? = null
            var quote: JSONObject? = null

            val url =
                JSONUtils().createURL(Constants.ShareMarket.API_BASE_URL_COMPANY_PROFILE + symbol)
            val qurl = JSONUtils().createURL(Constants.ShareMarket.API_BASE_URL_QUOTE + symbol)

            try {
                val jsonObject = JSONObject(JSONUtils().getJsonString(url))
                profile = jsonObject.getJSONObject("profile")
                val jsonArray = JSONArray(JSONUtils().getJsonString(qurl))
                quote = jsonArray.getJSONObject(0)

            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (profile != null && quote != null) {


                var exchange: String? = null
                try {
                    exchange = profile.getString("exchange")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var industry: String? = null
                try {
                    industry = profile.getString("industry")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var website: String? = null
                try {
                    website = profile.getString("website")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var description: String? = null
                try {
                    description = profile.getString("description")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var ceo: String? = null
                try {
                    ceo = profile.getString("ceo")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var sector: String? = null
                try {
                    sector = profile.getString("sector")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var price: Double? = null
                try {
                    price = quote.getDouble("price")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var changesPercentage: Double? = null
                try {
                    changesPercentage = quote.getDouble("changesPercentage")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var change: Double? = null
                try {
                    change = quote.getDouble("change")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var dayLow: Double? = null
                try {
                    dayLow = quote.getDouble("dayLow")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var dayHigh: Double? = null
                try {
                    dayHigh = quote.getDouble("dayHigh")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var yearHigh: Double? = null
                try {
                    yearHigh = quote.getDouble("yearHigh")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var yearLow: Double? = null
                try {
                    yearLow = quote.getDouble("yearLow")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var volume: Double? = null
                try {
                    volume = quote.getDouble("volume")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var avgVolume: Double? = null
                try {
                    avgVolume = quote.getDouble("avgVolume")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var open: Double? = null
                try {
                    open = quote.getDouble("open")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var previousClose: Double? = null
                try {
                    previousClose = quote.getDouble("previousClose")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                return Company(
                    exchange!!,
                    industry!!,
                    website!!,
                    description!!,
                    ceo!!,
                    sector!!,
                    price!!,
                    changesPercentage!!,
                    change!!,
                    dayLow!!,
                    dayHigh!!,
                    yearHigh!!,
                    yearLow!!,
                    volume!!,
                    avgVolume!!,
                    open!!,
                    previousClose!!
                )

            }
            return null
        }
    }

}
