package com.example.analytiq.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.analytiq.R
import com.example.analytiq.db.CurrencyDatabase
import com.example.analytiq.db.CurrencyEntity
import com.example.analytiq.model.Constants
import com.example.analytiq.util.ConnectivityManager
import com.jjoe64.graphview.GraphView
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TwoCurrenciesRelation : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var calculate: Button
    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner
    lateinit var result: TextView
    lateinit var edit1: EditText
    var firstSelected = 0
    var secondSelected = 0
    lateinit var progress: ProgressBar
    var dataList = ArrayList<jsonCurrency>()
    lateinit var refresh: SwipeRefreshLayout
    lateinit var updated: String
    lateinit var sharedPref: SharedPreferences
    lateinit var relative: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_currencies_relation)

        calculate = findViewById(R.id.calculate)
        result = findViewById(R.id.res_text)
        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        progress = findViewById(R.id.progress)
        edit1 = findViewById(R.id.et1)
        refresh = findViewById(R.id.refresh_layout)
        relative = findViewById(R.id.relative)
        sharedPref = getSharedPreferences("Updated", Context.MODE_PRIVATE)

        calculate.visibility = View.INVISIBLE
        result.visibility = View.INVISIBLE
        spinner1.visibility = View.INVISIBLE
        spinner2.visibility = View.INVISIBLE
        edit1.visibility = View.INVISIBLE
        relative.visibility = View.INVISIBLE
        updated = sharedPref.getString("lastUpdated", "NotAvailable").toString()

        if (ConnectivityManager().checkConnectivity(this)) {
            fetchData()
            findViewById<TextView>(R.id.no_internet).visibility = View.GONE
        } else {
            val listSaved = getCurrency(this).execute().get()
            if (listSaved.size == 0) {
                progress.visibility = View.GONE
                findViewById<TextView>(R.id.no_internet).visibility = View.VISIBLE
            } else {
                for (i in 0 until listSaved.size)
                    dataList.add(
                        jsonCurrency(
                            listSaved[i].code,
                            listSaved[i].name,
                            listSaved[i].rate
                        )
                    )

                calculate.visibility = View.VISIBLE
                result.visibility = View.VISIBLE
                spinner1.visibility = View.VISIBLE
                spinner2.visibility = View.VISIBLE
                edit1.visibility = View.VISIBLE
                relative.visibility = View.VISIBLE
                progress.visibility = View.GONE
                findViewById<TextView>(R.id.no_internet).visibility = View.VISIBLE

                val list = ArrayList<String>()
                for (j in 0 until dataList.size)
                    list.add(dataList[j].name)

                val arrayAdapter = myAdapter(this, dataList)

                spinner1.adapter = arrayAdapter
                spinner2.adapter = arrayAdapter

                spinner1.setOnItemSelectedListener(this)
                spinner2.onItemSelectedListener = this
            }
        }
        findViewById<ImageView>(R.id.swapImg).setOnClickListener {
            val f = firstSelected
            val s = secondSelected
            spinner1.setSelection(s)
            spinner2.setSelection(f)
        }

        calculate.setOnClickListener {
            if (edit1.text.isEmpty() || edit1.text.toString().toDoubleOrNull() == null) {
                Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show()
            } else {
                val entered = edit1.text.toString().toDouble()
                val firstrate = dataList[firstSelected].rate.toDouble()
                val secondrate = dataList[secondSelected].rate.toDouble()

                val relation = secondrate / firstrate
                val ans = entered * relation

                result.setText(
                    "" + entered + " " + dataList[firstSelected].code + " = " + "%.3f".format(ans) + " " + dataList[secondSelected].code + "\n\n1 " + dataList[firstSelected].code + " = " + "%.3f".format(
                        relation
                    ) + " " + dataList[secondSelected].code + "\n\n1 " + dataList[secondSelected].code + " = " + "%.3f".format(
                        1 / relation
                    ) + " " + dataList[firstSelected].code +
                            "\n\n\n Last updated on: " + updated
                )
                result.visibility = View.VISIBLE
            }
        }

        refresh.setOnRefreshListener {
            edit1.text.clear()
            if (ConnectivityManager().checkConnectivity(this)) {
                fetchData()
                findViewById<TextView>(R.id.no_internet).visibility = View.GONE
                Handler().postDelayed({refresh.isRefreshing = false},2000)
            } else {
                val listSaved = getCurrency(this).execute().get()
                if (listSaved.size == 0) {
                    progress.visibility = View.GONE
                    findViewById<TextView>(R.id.no_internet).visibility = View.VISIBLE
                } else {
                    for (i in 0 until listSaved.size)
                        dataList.add(
                            jsonCurrency(
                                listSaved[i].code,
                                listSaved[i].name,
                                listSaved[i].rate
                            )
                        )

                    calculate.visibility = View.VISIBLE
                    result.visibility = View.VISIBLE
                    spinner1.visibility = View.VISIBLE
                    spinner2.visibility = View.VISIBLE
                    edit1.visibility = View.VISIBLE
                    relative.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                    findViewById<TextView>(R.id.no_internet).visibility = View.VISIBLE

                    val list = ArrayList<String>()
                    for (j in 0 until dataList.size)
                        list.add(dataList[j].name)

                    val arrayAdapter = myAdapter(this, dataList)

                    spinner1.adapter = arrayAdapter
                    spinner2.adapter = arrayAdapter

                    spinner1.setOnItemSelectedListener(this)
                    spinner2.onItemSelectedListener = this
                }
                Handler().postDelayed({refresh.isRefreshing = false},2000)
            }
        }
    }

    fun fetchData() {
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from=INR&to=&amount=1.0"
        val jsonObject = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                try {
                    val success = it.getString("status")
                    if (success.equals("success")) {
                        updated = it.getString("updated_date")
                        sharedPref.edit().putString("lastUpdated", updated).apply()
                        val currencies = it.getJSONObject("rates")
                        val keyArray = ArrayList<String>()
                        val keys = currencies.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            keyArray.add(key)
                        }

                        val listSaved = getCurrency(this).execute().get()
                        for (i in 0 until listSaved.size) {
                            val singleEntity = listSaved[i]
                            changeCurrency(this, 2, singleEntity).execute().get()
                        }

                        for (i in 0 until keyArray.size) {
                            val indiJson = currencies.getJSONObject(keyArray[i])
                            val name = indiJson.getString("currency_name")
                            val cost = indiJson.getString("rate_for_amount")
                            val obj = jsonCurrency(keyArray[i], name, cost)
                            dataList.add(obj)

                            val entityObj = CurrencyEntity(i, name, keyArray[i], cost)
                            changeCurrency(this, 1, entityObj).execute().get()
                        }

                        calculate.visibility = View.VISIBLE
                        result.visibility = View.VISIBLE
                        spinner1.visibility = View.VISIBLE
                        spinner2.visibility = View.VISIBLE
                        edit1.visibility = View.VISIBLE
                        relative.visibility = View.VISIBLE
                        progress.visibility = View.GONE

                        val list = ArrayList<String>()
                        for (j in 0 until dataList.size)
                            list.add(dataList[j].name)

                        val arrayAdapter = myAdapter(this, dataList)

                        spinner1.adapter = arrayAdapter
                        spinner2.adapter = arrayAdapter

                        spinner1.setOnItemSelectedListener(this)
                        spinner2.onItemSelectedListener = this

                    } else {
                        Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["Content-type"] = "application/json"
                header["x-rapidapi-host"] = "currency-converter5.p.rapidapi.com"
                header["x-rapidapi-key"] = "7664c7d82dmsh4b3b729631c9965p10e9dajsn35ce3563b21a"
                return header
            }
        }

        queue.add(jsonObject)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (parent?.id == R.id.spinner1) {
            firstSelected = position

        } else if (parent?.id == R.id.spinner2) {
            secondSelected = position
        }
    }

    data class jsonCurrency(
        val code: String,
        val name: String,
        val rate: String
    )


    class myAdapter(val cntx: Context, val list: ArrayList<jsonCurrency>) :
        ArrayAdapter<jsonCurrency>(cntx, 0, list) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            return createView(position, convertView, parent)
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

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

            if (convertView == null) {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.currency_spinner, parent, false)

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

    class getCurrency(val context: Context) : AsyncTask<Void, Void, List<CurrencyEntity>>() {
        override fun doInBackground(vararg params: Void?): List<CurrencyEntity> {
            val room =
                Room.databaseBuilder(context, CurrencyDatabase::class.java, "currency-db").build()
            val dao = room.getDao()
            return dao.getAllCurrency()
        }
    }

    class changeCurrency(val context: Context, val mode: Int, val entity: CurrencyEntity) :
        AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val room =
                Room.databaseBuilder(context, CurrencyDatabase::class.java, "currency-db").build()
            val dao = room.getDao()

            when (mode) {
                1 -> {
                    dao.insertCurrency(entity)
                    return true
                }

                2 -> {
                    dao.deleteCurrency(entity)
                    return true
                }

                else -> return false
            }
        }
    }
}
