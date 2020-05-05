package com.example.analytiq.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.loader.content.AsyncTaskLoader
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.analytiq.R
import com.example.analytiq.model.Constants
import com.example.analytiq.util.ConnectivityManager
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CurrencyGraph : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner
    lateinit var graph: GraphView
    val spinner1List = ArrayList<jsonCurrency>()
    val list = ArrayList<String>()
    val keyArray = ArrayList<String>()
    var first = 0
    var second = 0
    lateinit var click: Button
    val currencies = ArrayList<Double>()
    var Cancelflag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_graph)

        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        graph = findViewById(R.id.graph)
        click = findViewById(R.id.button)

        list.add("5 days")
        list.add("10 days")
        list.add("20 days")
        list.add("50 days")
        list.add("100 days")
        list.add("150 days")
        list.add("200 days")
        list.add("365 days")
        val adapter2 =
            ArrayAdapter(this, R.layout.spinner_item, list)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2

        spinner1List.add(jsonCurrency("", "Select a currency"))
        var adapter1 = myAdapter(this, spinner1List)
        spinner1.adapter = adapter1

        findViewById<TextView>(R.id.no_internet).visibility = View.GONE
        findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this)
        val url =
            "https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from=INR&to=&amount=1.0"
        val jsonObj = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
            if (ConnectivityManager().checkConnectivity(this)) {
                findViewById<TextView>(R.id.no_internet).visibility = View.GONE
                try {

                    val success = it.getString("status")
                    if (success.equals("success")) {
                        val currencies = it.getJSONObject("rates")
                        val keys = currencies.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            keyArray.add(key)
                        }
                        spinner1List.clear()
                        for (i in 0 until keyArray.size) {
                            val indiJson = currencies.getJSONObject(keyArray[i])
                            val name = indiJson.getString("currency_name")
                            val obj = jsonCurrency(keyArray[i], name)
                            spinner1List.add(obj)
                        }
                        adapter1 = myAdapter(this, spinner1List)
                        spinner1.adapter = adapter1
                        spinner1.onItemSelectedListener = this
                        spinner2.onItemSelectedListener = this
                    } else {
                        Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                } catch (e: JSONException) {
                    Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                    return@Listener
                }
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                findViewById<TextView>(R.id.no_internet).visibility = View.VISIBLE
                click.visibility = View.INVISIBLE
                spinner1.visibility = View.INVISIBLE
                spinner2.visibility = View.INVISIBLE
                graph.visibility = View.INVISIBLE
                return@Listener
            }

        }, Response.ErrorListener {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
            return@ErrorListener
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["Content-type"] = "application/json"
                header["x-rapidapi-host"] = "currency-converter5.p.rapidapi.com"
                header["x-rapidapi-key"] = "7664c7d82dmsh4b3b729631c9965p10e9dajsn35ce3563b21a"
                return header
            }
        }
        queue.add(jsonObj)
        findViewById<ProgressBar>(R.id.progress).visibility = View.GONE

        click.setOnClickListener {

            if (ConnectivityManager().checkConnectivity(this)) {

                findViewById<TextView>(R.id.no_internet).visibility = View.GONE
                findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE
                findViewById<TextView>(R.id.graph_text).visibility = View.GONE
                findViewById<TextView>(R.id.pr_text).visibility = View.GONE
                graph.removeAllSeries()
                click.isClickable = false
                spinner1.isClickable = false
                spinner2.isClickable = false
                Cancelflag = 0

                val selectedDays: Int
                selectedDays = when (second) {
                    0 -> 5
                    1 -> 10
                    2 -> 20
                    3 -> 50
                    4 -> 100
                    5 -> 150
                    6 -> 200
                    7 -> 365
                    else -> 5
                }
                var completed = 0
                val selectedItem = spinner1List[first]
                val selectedCode = selectedItem.code
                jsonRetrieve(selectedCode, selectedDays, object : itemListener {
                    override fun itemAdded() {
                        completed++
                        GlobalScope.launch(Dispatchers.Main) {
                            val percent = (completed / selectedDays.toDouble()) * 100
                            findViewById<TextView>(R.id.pr_text).text =
                                "Loading..." + "%.1f".format(percent) + "%"
                            findViewById<TextView>(R.id.pr_text).visibility = View.VISIBLE
                            if (completed % 5 == 0) {
                                val c = completed
                                val dataPoint = Array(c, { o -> DataPoint(0.0, 0.0) })
                                for (z in 0 until c) {
                                    dataPoint[z] =
                                        DataPoint(
                                            (z + 1).toDouble(),
                                            currencies[currencies.size - 1 - z]
                                        )
                                }

                                val lineSeries = LineGraphSeries(dataPoint)
                                graph.removeAllSeries()
                                graph.addSeries(lineSeries)

                                graph.viewport.isXAxisBoundsManual = true
                                graph.viewport.isYAxisBoundsManual = true

                                graph.viewport.setMinX(0.0)
                                graph.viewport.setMaxX(completed.toDouble() + 1)
                                graph.viewport.setMinY(Collections.min(currencies))
                                graph.viewport.setMaxY(Collections.max(currencies))
                                graph.visibility = View.VISIBLE
                                findViewById<TextView>(R.id.graph_text).text =
                                    "Currently showing graph for $c days"
                                findViewById<TextView>(R.id.graph_text).visibility = View.VISIBLE
                            }
                        }
                    }
                }).execute()
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                findViewById<TextView>(R.id.no_internet).visibility = View.VISIBLE
                click.visibility = View.INVISIBLE
                spinner1.visibility = View.INVISIBLE
                spinner2.visibility = View.INVISIBLE
                graph.visibility = View.INVISIBLE
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (parent?.id == R.id.spinner1) {
            first = position
        } else if (parent?.id == R.id.spinner2) {
            second = position
        }
    }

    interface itemListener {
        fun itemAdded()
    }

    inner class jsonRetrieve(
        val code: String,
        val days: Int,
        val listener: itemListener
    ) :
        AsyncTask<Void, Void, ArrayList<Double>>() {

        val curr = ArrayList<Double>()
        var flag = 0
        override fun doInBackground(vararg params: Void?): ArrayList<Double> {
            var date: String
            val calendar = Calendar.getInstance()
            val dates = ArrayList<String>()
            for (i in 0 until days) {
                dates.add(SimpleDateFormat("yyyy-MM-dd").format(calendar.time))
                calendar.add(Calendar.DATE, -1)
            }
            for (i in 0 until days) {
                date = dates[i]
                val url =
                    "https://currency-converter5.p.rapidapi.com/currency/historical/" + date + "?format=json&to=INR&from=" + code + "&amount=1.0"

                val client = OkHttpClient()
                val request = okhttp3.Request.Builder().url(url).get()
                    .addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", Constants.Currency.API_KEY)
                    .build()
                var string: String? = null
                var jsonObject: JSONObject? = null
                try {
                    val response = client.newCall(request).execute()
                    string = response.body()?.string()
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
                if (jsonObject != null) {
                    val rates: JSONObject
                    try {
                        rates = jsonObject.getJSONObject("rates")
                        val currency = rates.getJSONObject("INR")
                        val currencyValue = currency.getDouble("rate")
                        curr.add(currencyValue)
                        println("Size is ${curr.size}")
                        currencies.add(currencyValue)
                        listener.itemAdded()
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                    }
                }
                if (Cancelflag == 1 || isCancelled) {
                    break
                }
            }
            if (curr.size == days)
                flag = 1
            else
                this.cancel(true)
            return curr
        }

        override fun onCancelled() {
            graph.viewport.isScrollable = true
            graph.viewport.isScalable = true
            graph.viewport.isXAxisBoundsManual = true
            graph.viewport.isYAxisBoundsManual = true

            graph.viewport.setMinX(0.0)
            graph.viewport.setMaxX(days.toDouble() + 1)

            findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
            findViewById<TextView>(R.id.pr_text).visibility = View.GONE
            click.isClickable = true
            findViewById<TextView>(R.id.graph_text).text =
                "Graph for ${curr.size} days"
            spinner1.isClickable = true
            spinner2.isClickable = true
            super.onCancelled()
        }

        override fun onPostExecute(result: ArrayList<Double>?) {
            if (flag == 1 && Cancelflag == 0) {
                findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE
                val dataPoint = Array(days, { o -> DataPoint(0.0, 0.0) })
                var mini = 1000.0
                var maxi = 0.0
                for (z in 0 until curr.size) {
                    if (curr[z] > maxi)
                        maxi = curr[z]
                    if (curr[z] < mini)
                        mini = curr[z]
                    dataPoint[z] =
                        DataPoint(
                            (z + 1).toDouble(),
                            curr[curr.size - 1 - z]
                        )
                }
                val lineSeries = LineGraphSeries(dataPoint)
                graph.removeAllSeries()
                graph.addSeries(lineSeries)

                graph.viewport.isScrollable = true
                graph.viewport.isScalable = true
                graph.viewport.isXAxisBoundsManual = true
                graph.viewport.isYAxisBoundsManual = true

                graph.viewport.setMinX(0.0)
                graph.viewport.setMaxX(days.toDouble() + 1)
                graph.viewport.setMinY(mini)
                graph.viewport.setMaxY(maxi)

                graph.visibility = View.VISIBLE
                findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
                findViewById<TextView>(R.id.pr_text).visibility = View.GONE
                findViewById<TextView>(R.id.graph_text).text =
                    "Graph for ${curr.size} days"
                click.isClickable = true
                spinner1.isClickable = true
                spinner2.isClickable = true
            }
        }
    }

    data class jsonCurrency(
        val code: String,
        val name: String
    )

    class myAdapter(val cntx: Context, val list: ArrayList<jsonCurrency>) :
        ArrayAdapter<jsonCurrency>(cntx, 0, list) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            if (convertView == null) {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)

                val name: TextView = view.findViewById(R.id.name)

                val item = list[position]
                name.setText(item.name)
                return view
            } else {
                val name: TextView = convertView.findViewById(R.id.name)

                val item = list[position]
                name.setText(item.name)
                return convertView
            }
        }

        override fun getDropDownView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {

            if (convertView == null) {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.currency_spinner, parent, false)

                val name: TextView = view.findViewById(R.id.t_name)
                val code: TextView = view.findViewById(R.id.t_code)

                val item = list[position]
                name.setText(item.name)
                code.setText(item.code)
                return view
            } else {
                val name: TextView = convertView.findViewById(R.id.t_name)
                val code: TextView = convertView.findViewById(R.id.t_code)

                val item = list[position]
                name.setText(item.name)
                code.setText(item.code)
                return convertView
            }
        }
    }

    override fun onStop() {
        Cancelflag = 1
        super.onStop()
    }

    override fun onBackPressed() {
        Cancelflag = 1
        super.onBackPressed()
    }
}
