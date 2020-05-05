package com.example.analytiq.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.analytiq.R
import com.example.analytiq.model.Company
import android.widget.LinearLayout
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.analytiq.util.ConnectivityManager
import org.json.JSONException
import org.json.JSONObject
import com.example.analytiq.model.JSONUtils
import org.json.JSONArray
import androidx.loader.content.AsyncTaskLoader
import com.example.analytiq.model.Constants
import java.io.IOException
import java.text.DecimalFormat

class CategoryShareMarket : AppCompatActivity(), LoaderManager.LoaderCallbacks<ArrayList<Company>> {

    var list = ArrayList<Company>()
    var displayList = ArrayList<Company>()
    lateinit var progressBar: ProgressBar
    lateinit var noData: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView
    lateinit var adapter: CompanyAdapter
    val CHANGE_VALUE = 1
    val NO_CHANGE_VALUE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_share_market)

        val intent = intent
        val title = intent.getStringExtra("title")

        noData = findViewById(R.id.no_data)
        progressBar = findViewById(R.id.progressbar)
        noData.text = null
        progressBar.visibility = View.GONE
        val cardView: CardView = findViewById(R.id.search_bar_cd)
        cardView.setVisibility(View.GONE)
        searchView = findViewById(R.id.search_bar)
        val Title = findViewById<TextView>(R.id.title)
        Title.text = title
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val temp = ArrayList<Company>()
                for (c in list) {

                    if (c.getCompanyName().toLowerCase().contains(newText.toLowerCase().trim { it <= ' ' }) ||
                        c.getTicker().toLowerCase().contains(newText.toLowerCase().trim { it <= ' ' }) ||
                        c.getExchange().toLowerCase().contains(newText.toLowerCase().trim { it <= ' ' })
                    ) {
                        temp.add(c)
                    }
                }
                displayList.clear()
                displayList.addAll(temp)
                adapter.notifyDataSetChanged()
                return false
            }
        })

        assert(title != null)
        if (title == "All Companies") {
            cardView.setVisibility(View.VISIBLE)
            if (ConnectivityManager().checkConnectivity(this)) {
                noData.text = null
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                supportLoaderManager.initLoader(1, null, this)
                list = ArrayList()
                displayList = ArrayList()
                adapter = CompanyAdapter(this, displayList, NO_CHANGE_VALUE)
                recyclerView.adapter = adapter
            } else {
                noData.text = "No internet Connection"
                progressBar.visibility = View.GONE
            }
        } else {
            list = intent.getSerializableExtra("list") as ArrayList<Company>
            val adapter = CompanyAdapter(this, list, CHANGE_VALUE)
            recyclerView.adapter = adapter
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Company>> {
        return CompanyLoader(this)
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Company>>, data: ArrayList<Company>?) {
        if (data?.size != 0) {
            noData.setText(null)
            progressBar.setVisibility(View.GONE)
            recyclerView.setVisibility(View.VISIBLE)
            list.clear()
            if (data != null) {
                list.addAll(data)
            }
            displayList.clear()
            displayList.addAll(list)
            adapter.notifyDataSetChanged()
        } else {
            noData.setText("Currently no data is available")
            progressBar.setVisibility(View.GONE)
            recyclerView.setVisibility(View.GONE)
        }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Company>>) {
        list.clear()
        displayList.clear()
    }

    private class CompanyLoader(context: Context) : AsyncTaskLoader<ArrayList<Company>>(context) {


        override fun loadInBackground(): ArrayList<Company>? {
            return fetchCompanyData()
        }

        override fun onStartLoading() {
            super.onStartLoading()
            forceLoad()
        }


        fun fetchCompanyData(): ArrayList<Company> {

            val decimalFormat = DecimalFormat("#.###")
            val companyData = ArrayList<Company>()

            var jsonArray: JSONArray? = null

            val url = JSONUtils().createURL(Constants.ShareMarket.API_STOCK_LIST)
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(JSONUtils().getJsonString(url))
                jsonArray = jsonObject!!.getJSONArray("symbolsList")
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }


            if (jsonArray != null) {

                for (i in 0 until jsonArray.length()) {

                    var `object`: JSONObject? = null
                    try {
                        `object` = jsonArray.getJSONObject(i)
                        val symbol = `object`!!.getString("symbol")
                        val price = `object`.getDouble("price")
                        val name = `object`.getString("name")
                        val exchange = `object`.getString("exchange")

                        companyData.add(Company(symbol, name, price, exchange))

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            return companyData
        }
    }

    class CompanyAdapter(
        var mContext: Context,
        var mCompanies: ArrayList<Company>,
        var mIsChangeHaving: Int
    ) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

        val CHANGE_VALUE = 1
        val NO_CHANGE_VALUE = 0

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CompanyAdapter.ViewHolder {
            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.share_market_recyclerview_child, parent, false)
            return CompanyAdapter.ViewHolder(view)
        }

        override fun onBindViewHolder(holder: CompanyAdapter.ViewHolder, position: Int) {

            val company = mCompanies[position]
            var mcompanyName: String? = null
            if (company.getCompanyName() != "null") {
                mcompanyName = company.getCompanyName()
            }
            val mvalue = company.getPrice()
            val mticker = company.getTicker()
            if (mIsChangeHaving == CHANGE_VALUE) {
                val mchangePercent =
                    company.getChangePercent().substring(1, company.getChangePercent().length - 1)
                val mchange = company.getChange()

                if (mchange >= 0) {
                    holder.changePercent.setTextColor(Color.parseColor("#00C853"))
                    holder.change.setTextColor(Color.parseColor("#00C853"))
                } else {
                    holder.changePercent.setTextColor(Color.parseColor("#FF3D00"))
                    holder.change.setTextColor(Color.parseColor("#FF3D00"))
                }
                holder.changePercent.text = mchangePercent
                holder.change.text = mchange.toString()
            } else {
                holder.value.gravity = Gravity.CENTER
                holder.changeLayout.visibility = View.GONE
            }

            holder.companyName.text = mcompanyName
            holder.value.text = mvalue.toString()
            holder.ticker.text = mticker


            val McompanyName = mcompanyName
            holder.layout.setOnClickListener {
                val intent = Intent(mContext, SingleCompany::class.java)
                intent.putExtra("Symbol", mticker)
                intent.putExtra("CompanyName", McompanyName)
                mContext.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return mCompanies.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var layout: LinearLayout = itemView as LinearLayout
            var changeLayout: LinearLayout = itemView.findViewById(R.id.change_layout)
            var companyName: TextView = itemView.findViewById(R.id.company_name)
            var value: TextView = itemView.findViewById(R.id.value)
            var ticker: TextView = itemView.findViewById(R.id.ticker)
            var changePercent: TextView = itemView.findViewById(R.id.change_percent)
            var change: TextView = itemView.findViewById(R.id.change)
        }
    }
}
