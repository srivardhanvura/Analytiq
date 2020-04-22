package com.example.analytiq.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.widget.LinearLayout
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import org.json.JSONException
import org.json.JSONObject
import okhttp3.OkHttpClient
import android.widget.Toast
import com.example.analytiq.util.ConnectivityManager
import android.view.View
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.analytiq.R
import com.example.analytiq.model.Constants
import okhttp3.Request
import java.io.IOException
import java.text.DecimalFormat

class CurrencyConversion : AppCompatActivity(),
    LoaderManager.LoaderCallbacks<ArrayList<CurrencyConversion.Currency>> {

    //    val LOG_TAG:String = CurrencyConversion.class.getSimpleName()
    lateinit var currencyRecyclerView: RecyclerView
    lateinit var noData: TextView
    lateinit var progressBar: ProgressBar
    lateinit var refreshLayout: SwipeRefreshLayout
    var currencyData = ArrayList<Currency>()
    lateinit var currencyAdapter: CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_conversion)

        refreshLayout = findViewById(R.id.refresh_layout)
        progressBar = findViewById(R.id.progress_circular)
        noData = findViewById(R.id.no_data)
        currencyRecyclerView = findViewById(R.id.currency_recyclerview)
        currencyRecyclerView.layoutManager = LinearLayoutManager(this)
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(currencyRecyclerView.getContext(),DividerItemDecoration.VERTICAL);
//        currencyRecyclerView.addItemDecoration(dividerItemDecoration);

        var isConnected = ConnectivityManager().checkConnectivity(this)
        if (isConnected) {

            progressBar.visibility = View.VISIBLE
            noData.text = null
            supportLoaderManager.initLoader(1, null, this)

        } else {
            progressBar.visibility = View.GONE
            noData.text = "No Internet Connection found!"

        }

        currencyAdapter = CurrencyAdapter(this, currencyData)
        currencyRecyclerView.adapter = currencyAdapter

        refreshLayout.setOnRefreshListener {
            // Restart Loader
            isConnected = ConnectivityManager().checkConnectivity(this)
            if (isConnected) {
                noData.text = null
                supportLoaderManager.restartLoader(1, null, this)
            } else {
                if (refreshLayout.isRefreshing) {
                    refreshLayout.isRefreshing = false
                }
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@CurrencyConversion,
                    "No Internet Connection found!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Currency>> {
        return CurrencyLoader(this)
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Currency>>, data: ArrayList<Currency>?) {
        if (data?.size != 0) {
            if (refreshLayout.isRefreshing) {
                refreshLayout.isRefreshing = false
            }
            progressBar.visibility = View.GONE
            noData.text = null
            currencyData.clear()
            if (data != null) {
                currencyData.addAll(data)
            }
            currencyAdapter.notifyDataSetChanged()
        } else {
            if (refreshLayout.isRefreshing) {
                refreshLayout.isRefreshing = false
            }
            progressBar.visibility = View.GONE
            if (currencyData.size == 0) {
                noData.text = "Sorry, Currently no data is available"
            }
        }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Currency>>) {
        currencyData.clear()
    }

    private class CurrencyLoader(context: Context) : AsyncTaskLoader<ArrayList<Currency>>(context) {

        override fun loadInBackground(): ArrayList<Currency>? {
            return fetchCurrencyData()
        }

        override fun onStartLoading() {
            super.onStartLoading()
            forceLoad()
        }

        fun fetchCurrencyData(): ArrayList<Currency> {
            val decimalFormat = DecimalFormat("#.###")
            val currencyData = ArrayList<Currency>()

            //        if (TextUtils.isEmpty(url)) {
            //            return null;
            //        }
            //        URL jsonURL = JSONUtils.createURL(url);
            val baseCurrencyCode = "INR"
            val amount = 1.0
            val jsonObject = getJSONObjectConvert(baseCurrencyCode, "", amount)

            val rates: JSONObject
            try {
                rates = jsonObject!!.getJSONObject("rates")

                val keyArray = ArrayList<String>()
                val keys = rates.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    keyArray.add(key)
                }
                for (i in 0 until keyArray.size) {
                    val key = keyArray.get(i)
                    if (key != "INR") {
                        val currency: JSONObject
                        try {
                            currency = rates.getJSONObject(key)
                            val currencyName = currency.getString("currency_name")
                            val currencyValue = 1 / currency.getDouble("rate")
                            currencyData.add(
                                Currency(
                                    "1 $key",
                                    "₹ " + decimalFormat.format(currencyValue),
                                    currencyName
                                )
                            )
                        } catch (ex: JSONException) {
                            ex.printStackTrace()
                        }

                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return currencyData

        }

        private fun getJSONObjectList(): JSONObject? {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(Constants.Currency.API_BASE_URL + Constants.Currency.API_LIST)
                .get()
                .addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
                .addHeader("x-rapidapi-key", Constants.Currency.API_KEY)
                .build()

            var string: String? = null
            try {
                val response = client.newCall(request).execute()
                string = response.body().string()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            var list: JSONObject? = null
            try {
                val jsonObject = JSONObject(string!!.trim { it <= ' ' })
                list = jsonObject.getJSONObject("currencies")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return list
        }

        private fun getJSONObjectConvert(from: String, to: String, amount: Double): JSONObject? {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(Constants.Currency.API_BASE_URL + Constants.Currency.API_CONVERT + "&from=" + from + "&to=" + to + "&amount=" + amount)
                .get()
                .addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
                .addHeader("x-rapidapi-key", Constants.Currency.API_KEY)
                .build()

            var string: String? = null
            var jsonObject: JSONObject? = null
            try {
                val response = client.newCall(request).execute()
                string = response.body().string()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (string != null) {
                        jsonObject = JSONObject(string)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return jsonObject
        }
    }
//    public fun fetchCurrencyData(): ArrayList<Currency> {
//
//        val decimalFormat = DecimalFormat("#.###")
//        val currencyData = ArrayList<Currency>()
//
//        //        if (TextUtils.isEmpty(url)) {
//        //            return null;
//        //        }
//        //        URL jsonURL = JSONUtils.createURL(url);
//        val baseCurrencyCode = "INR"
//        val amount = 1.0
//        val jsonObject = getJSONObjectConvert(baseCurrencyCode, "", amount)
//
//        val rates: JSONObject
//        try {
//            rates = jsonObject!!.getJSONObject("rates")
//
//            val keyArray = ArrayList<String>()
//            val keys = rates.keys()
//            while (keys.hasNext()) {
//                val key = keys.next()
//                keyArray.add(key)
//            }
//            for (i in 0 until keyArray.size) {
//                val key = keyArray.get(i)
//                if (key != "INR") {
//                    val currency: JSONObject
//                    try {
//                        currency = rates.getJSONObject(key)
//                        val currencyName = currency.getString("currency_name")
//                        val currencyValue = 1 / currency.getDouble("rate")
//                        currencyData.add(
//                            Currency(
//                                "1 $key",
//                                "₹ " + decimalFormat.format(currencyValue),
//                                currencyName
//                            )
//                        )
//                    } catch (ex: JSONException) {
//                        ex.printStackTrace()
//                    }
//
//                }
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        /**    try {
//         * JSONObject jsonObject = new JSONObject(JSONUtils.getJsonString(jsonURL));
//         * JSONObject rates = jsonObject.getJSONObject("rates");
//         * double INR = rates.getDouble("INR");
//         * double USD = INR / 1;
//         * double EUR = INR / rates.getDouble("EUR");
//         * double CNY = INR / rates.getDouble("CNY");
//         * double JPY = INR / rates.getDouble("JPY");
//         * double GBP = INR / rates.getDouble("GBP");
//         * double AUD = INR / rates.getDouble("AUD");
//         * double SGD = INR / rates.getDouble("SGD");
//         * double TWD = INR / rates.getDouble("TWD");
//         * double KWD = INR / rates.getDouble("KWD");
//         * double IRR = INR / rates.getDouble("IRR");
//         * double AED = INR / rates.getDouble("AED");
//         * double LKR = INR / rates.getDouble("LKR");
//         * double SAR = INR / rates.getDouble("SAR");
//         * double HKD = INR / rates.getDouble("HKD");
//         * double BDT = INR / rates.getDouble("BDT");
//         * double CAD = INR / rates.getDouble("CAD");
//         * double EGP = INR / rates.getDouble("EGP");
//         * double NZD = INR / rates.getDouble("NZD");
//         * double RUB = INR / rates.getDouble("RUB");
//         * //                JSONObject RMB = rates.getJSONObject("RMB");
//         * currencyData.add(new Currency("1 USD", "₹ " + decimalFormat.format(USD), "US Dollar"));
//         * currencyData.add(new Currency("1 EUR", "₹ " + decimalFormat.format(EUR), "Euro"));
//         * currencyData.add(new Currency("1 CNY", "₹ " + decimalFormat.format(CNY), "Chinese Yuan"));
//         * currencyData.add(new Currency("1 JPY", "₹ " + decimalFormat.format(JPY), "Japanese Yen"));
//         * currencyData.add(new Currency("1 GBP", "₹ " + decimalFormat.format(GBP), "Pound Sterling"));
//         * currencyData.add(new Currency("1 AUD", "₹ " + decimalFormat.format(AUD), "Australian Dollar"));
//         * currencyData.add(new Currency("1 SGD", "₹ " + decimalFormat.format(SGD), "Singapore Dollar"));
//         * currencyData.add(new Currency("1 TWD", "₹ " + decimalFormat.format(TWD), "New Taiwan Dollar"));
//         * currencyData.add(new Currency("1 KWD", "₹ " + decimalFormat.format(KWD), "Kuwaiti Dinar"));
//         * currencyData.add(new Currency("1 IRR", "₹ " + decimalFormat.format(IRR), "Iranian Rial"));
//         * currencyData.add(new Currency("1 AED", "₹ " + decimalFormat.format(AED), "United Arab Emirates Dirham"));
//         * currencyData.add(new Currency("1 LKR", "₹ " + decimalFormat.format(LKR), "Sri Lankan Rupee"));
//         * currencyData.add(new Currency("1 SAR", "₹ " + decimalFormat.format(SAR), "Saudi Riyal"));
//         * currencyData.add(new Currency("1 HKD", "₹ " + decimalFormat.format(HKD), "Hong Kong Dollar"));
//         * currencyData.add(new Currency("1 BDT", "₹ " + decimalFormat.format(BDT), "Bangladeshi Taka"));
//         * currencyData.add(new Currency("1 CAD", "₹ " + decimalFormat.format(CAD), "Canadian Dollar"));
//         * currencyData.add(new Currency("1 EGP", "₹ " + decimalFormat.format(EGP), "Egyptian Pound"));
//         * currencyData.add(new Currency("1 NZD", "₹ " + decimalFormat.format(NZD), "New Zealand Dollar"));
//         * currencyData.add(new Currency("1 RUB", "₹ " + decimalFormat.format(RUB), "Russian Ruble"));
//         * } catch (JSONException e) {
//         * Log.e(LOG_TAG, "Problem parsing currency JSON result ", e);
//         * } catch (IOException e) {
//         * e.printStackTrace();
//         * }
//         */
//        return currencyData
//    }


//
//    private fun getJSONObjectList(): JSONObject? {
//        val client = OkHttpClient()
//
//        val request = Request.Builder()
//            .url(Constants.Currency.API_BASE_URL + Constants.Currency.API_LIST)
//            .get()
//            .addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
//            .addHeader("x-rapidapi-key", Constants.Currency.API_KEY)
//            .build()
//
//        var string: String? = null
//        try {
//            val response = client.newCall(request).execute()
//            string = response.body().string()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        var list: JSONObject? = null
//        try {
//            val jsonObject = JSONObject(string!!.trim { it <= ' ' })
//            list = jsonObject.getJSONObject("currencies")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        return list
//    }


    inner class CurrencyAdapter(
        var mContext: Context, var mCurrencyData: ArrayList<Currency>
    ) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.currency_recyclerview_child, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val currency = mCurrencyData[position]
            val currencyName = currency.getCurrencyName()
            val currencyValue = currency.getCurrencyValue()
            val Currency = currency.getCurrency()

            holder.currencyName.text = currencyName
            holder.currencyValue.text = currencyValue
            holder.currency.text = Currency

            holder.layout.setOnClickListener {
                val intent = Intent(mContext, SingleCurrency::class.java)
                intent.putExtra("Currency", Currency)
                intent.putExtra("CurrencyName", currencyName)
                intent.putExtra("CurrencyValue", currencyValue)
                startActivity(intent)

            }

        }

        override fun getItemCount(): Int {
            return mCurrencyData.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var layout: LinearLayout = itemView.findViewById(R.id.layout)
            var currencyName: TextView = itemView.findViewById(R.id.currency_name)
            var currencyValue: TextView = itemView.findViewById(R.id.currency)
            var currency: TextView = itemView.findViewById(R.id.value)
        }

    }

    class Currency(currency: String, currencyValue: String, currencyName: String) {

        var mCurrencyName = currencyName
        var mCurrencyValue = currencyValue
        var mCurrency = currency

        fun getCurrencyName(): String {
            return mCurrencyName
        }

        fun getCurrencyValue(): String {
            return mCurrencyValue
        }

        fun getCurrency(): String {
            return mCurrency
        }
    }
}
