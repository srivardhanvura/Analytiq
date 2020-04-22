package com.example.analytiq.fragment


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.analytiq.R
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.analytiq.fragment.SingleCurrencyHistoricalList.SingleCurrencyAdapter.ViewHolder
import com.example.analytiq.model.Constants

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class SingleCurrencyHistoricalList(currency: String, currentDate: Date, private val mMode: Int) :
    Fragment(), LoaderManager.LoaderCallbacks<ArrayList<SingleCurrencyHistoricalList.Currency>> {

    private var progressBar: ProgressBar? = null
    private var noData: TextView? = null
    private var singleCurrencyAdapter: SingleCurrencyAdapter? = null
    private val currencyData = ArrayList<Currency>()
    val mCurrentDate = currentDate
    val mCurrency = currency

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_single_currency_historical_list, container, false)

        progressBar = view.findViewById(R.id.progress_circular)
        noData = view.findViewById(R.id.no_data)
        val singleCurrencyRecyclerview =
            view.findViewById<RecyclerView>(R.id.single_currency_recyclerview)
        singleCurrencyRecyclerview.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(
            singleCurrencyRecyclerview.context,
            DividerItemDecoration.VERTICAL
        )
        singleCurrencyRecyclerview.addItemDecoration(dividerItemDecoration)
        val isConnected =
            com.example.analytiq.util.ConnectivityManager().checkConnectivity(activity as Context)

        if (isConnected) {

            progressBar!!.visibility = View.VISIBLE
            noData!!.text = null
            when (mMode) {
                1 -> activity!!.supportLoaderManager.initLoader<ArrayList<Currency>>(1, null, this)
                2 -> activity!!.supportLoaderManager.initLoader<ArrayList<Currency>>(2, null, this)
                3 -> activity!!.supportLoaderManager.initLoader<ArrayList<Currency>>(3, null, this)
                else -> {
                }
            }

        } else {
            progressBar!!.visibility = View.GONE
            if (currencyData.size == 0) {
                noData!!.text = "No Internet Connection found!"
            }

        }

        singleCurrencyAdapter = SingleCurrencyAdapter(activity as Context, currencyData)
        singleCurrencyRecyclerview.adapter = singleCurrencyAdapter
        return view
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Currency>> {

        return CurrencyDataLoader(context!!, id,mCurrency,mCurrentDate)

    }

    override fun onLoadFinished(loader: Loader<ArrayList<Currency>>, data: ArrayList<Currency>) {
        if (data.size != 0) {
            progressBar!!.visibility = View.GONE
            noData!!.text = null
            currencyData.clear()
            currencyData.addAll(data)

            singleCurrencyAdapter!!.notifyDataSetChanged()
        } else {
            progressBar!!.visibility = View.GONE
            if (currencyData.size == 0) {
                noData!!.text = "Sorry, Currently no data is available"
            }
        }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Currency>>) {
        currencyData.clear()
    }

    private class CurrencyDataLoader(context: Context, internal var mId: Int,currency:String,currentDate:Date) :
        AsyncTaskLoader<ArrayList<Currency>>(context) {

        override fun onStartLoading() {
            super.onStartLoading()
            forceLoad()
        }

        override fun loadInBackground(): ArrayList<Currency>? {
            return fetchCurrencyData(mId)
        }
        val mCurrency=currency
        val mCurrentDate=currentDate

        private fun fetchCurrencyData(mId: Int): ArrayList<SingleCurrencyHistoricalList.Currency>? {
            val decimalFormat = DecimalFormat("#.###")
            val currencyData = ArrayList<Currency>()
            val baseCurrencyCode = "INR"
            val amount = 1.0
            val desiredCurrency = mCurrency.substring(2)
            val calendar = Calendar.getInstance()
            calendar.time = mCurrentDate

            val LIMIT: Int
            when (id) {
                1 -> LIMIT = 30
                2 -> LIMIT = 24
                3 -> LIMIT = 36
                else -> LIMIT = 0
            }
            for (i in 0 until LIMIT) {

                when (id) {
                    1 -> calendar.add(Calendar.DATE, -1)
                    2 -> calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1)
                    3 -> calendar.add(Calendar.MONTH, -1)
                    else -> {
                    }
                }
                val date = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
                val inputDate = SimpleDateFormat("MMM dd, yyyy").format(calendar.time)
                val jsonObject =
                    getJSONObjectHistory(date, baseCurrencyCode, desiredCurrency, amount)
                if (jsonObject != null) {
                    val rates: JSONObject
                    try {
                        rates = jsonObject.getJSONObject("rates")
                        val currency = rates.getJSONObject(desiredCurrency)
                        val currencyValue = 1 / currency.getDouble("rate")
                        currencyData.add(
                            Currency(
                                inputDate,
                                "₹ " + decimalFormat.format(currencyValue)
                            )
                        )

                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                    }

                }
            }
            return currencyData
        }

        private fun getJSONObjectHistory(
            date: String,
            from: String,
            to: String,
            amount: Double
        ): JSONObject? {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(Constants.Currency.API_BASE_URL + Constants.Currency.API_HISTORY_INITIAL + date + Constants.Currency.API_HISTORY_END + "&to=" + to + "&from=" + from + "&amount=" + amount)
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

    private class SingleCurrencyAdapter(
        internal var mContext: Context,
        internal var mCurrencyData: ArrayList<Currency>
    ) : RecyclerView.Adapter<ViewHolder>() {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {

            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_currency_recyclerview_child, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val currency = mCurrencyData[position]
            val currencyValue = currency.getCurrencyValue()
            val date = currency.getdate()

            holder.currencyValue.text = currencyValue
            holder.date.text = date

        }

        override fun getItemCount(): Int {
            return mCurrencyData.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var currencyValue: TextView
            var date: TextView

            init {

                date = itemView.findViewById<View>(R.id.date) as TextView
                currencyValue = itemView.findViewById<View>(R.id.value) as TextView
            }

        }

    }

    class Currency(date: String, currencyValue: String) {

        var mDate = date
        var mCurrencyValue = currencyValue

        fun getdate(): String {
            return mDate
        }

        fun getCurrencyValue(): String {
            return mCurrencyValue
        }

//        fun fetchCurrencyData(id: Int): ArrayList<Currency> {
//
//            val decimalFormat = DecimalFormat("#.###")
//            val currencyData = ArrayList<Currency>()
//            val baseCurrencyCode = "INR"
//            val amount = 1.0
//            val desiredCurrency = mCurrency.substring(2)
//            val calendar = Calendar.getInstance()
//            calendar.time = mCurrentDate
//
//            val LIMIT: Int
//            when (id) {
//                1 -> LIMIT = 30
//                2 -> LIMIT = 24
//                3 -> LIMIT = 36
//                else -> LIMIT = 0
//            }
//            for (i in 0 until LIMIT) {
//
//                when (id) {
//                    1 -> calendar.add(Calendar.DATE, -1)
//                    2 -> calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1)
//                    3 -> calendar.add(Calendar.MONTH, -1)
//                    else -> {
//                    }
//                }
//                val date = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
//                val inputDate = SimpleDateFormat("MMM dd, yyyy").format(calendar.time)
//                val jsonObject =
//                    getJSONObjectHistory(date, baseCurrencyCode, desiredCurrency, amount)
//                if (jsonObject != null) {
//                    val rates: JSONObject
//                    try {
//                        rates = jsonObject.getJSONObject("rates")
//                        val currency = rates.getJSONObject(desiredCurrency)
//                        val currencyValue = 1 / currency.getDouble("rate")
//                        currencyData.add(
//                            Currency(
//                                inputDate,
//                                "₹ " + decimalFormat.format(currencyValue)
//                            )
//                        )
//
//                    } catch (ex: JSONException) {
//                        ex.printStackTrace()
//                    }
//
//                }
//            }
//            return currencyData
//        }
//
//        private fun getJSONObjectHistory(
//            date: String,
//            from: String,
//            to: String,
//            amount: Double
//        ): JSONObject? {
//            val client = OkHttpClient()
//
//            val request = Request.Builder()
//                .url(Constants.Currency.API_BASE_URL + Constants.Currency.API_HISTORY_INITIAL + date + Constants.Currency.API_HISTORY_END + "&to=" + to + "&from=" + from + "&amount=" + amount)
//                .get()
//                .addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", Constants.Currency.API_KEY)
//                .build()
//
//            var string: String? = null
//            var jsonObject: JSONObject? = null
//            try {
//                val response = client.newCall(request).execute()
//                string = response.body().string()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            } finally {
//                try {
//                    if (string != null) {
//                        jsonObject = JSONObject(string)
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//            }
//            return jsonObject
//        }
    }
}