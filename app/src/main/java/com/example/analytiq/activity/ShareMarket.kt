package com.example.analytiq.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.analytiq.R
import com.example.analytiq.model.Company
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import org.json.JSONException
import org.json.JSONObject
import com.example.analytiq.model.JSONUtils
import org.json.JSONArray
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import android.util.TypedValue
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import com.example.analytiq.model.Constants
import com.example.analytiq.util.ConnectivityManager
import java.io.IOException
import java.net.URL
import java.text.DecimalFormat

class ShareMarket : AppCompatActivity(), LoaderManager.LoaderCallbacks<ArrayList<Company>> {

    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var progressBar: ProgressBar
    lateinit var noData: TextView
    lateinit var activeRecyclerView: RecyclerView
    lateinit var gainerRecyclerView: RecyclerView
    lateinit var loserRecyclerView: RecyclerView
    val gainerL = ArrayList<Company>()
    val loserL = ArrayList<Company>()
    val activeL = ArrayList<Company>()
    lateinit var active: View
    lateinit var gainer: View
    lateinit var loser: View
    lateinit var all: LinearLayout
    lateinit var activeAdapter: CompanyAdapter
    lateinit var gainerAdapter: CompanyAdapter
    lateinit var loserAdapter: CompanyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_market)

//        refreshLayout = findViewById(R.id.refresh_layout)
        val container = findViewById<LinearLayout>(R.id.container)
        progressBar = findViewById(R.id.progress_circular)
        noData = findViewById(R.id.no_data)
        all = findViewById(R.id.all)
        all.setOnClickListener {
            val intent = Intent(this@ShareMarket, CategoryShareMarket::class.java)
            intent.putExtra("title", "All Companies")
            startActivity(intent)
        }
        val margin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
                .toInt()
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(margin, margin, margin, 0)

        active = layoutInflater.inflate(R.layout.share_market_recycler_view, null)
        active.layoutParams = layoutParams
        active.visibility = View.GONE
        val activeTitle = active.findViewById<TextView>(R.id.title)
        activeTitle.text = "Most Active"
        val viewMostActive: TextView = active.findViewById(R.id.view_more)
        viewMostActive.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ShareMarket, CategoryShareMarket::class.java)
            intent.putExtra("title", activeTitle.text as String)
            intent.putExtra("list", activeL)
            startActivity(intent)
        })
        activeRecyclerView = active.findViewById(R.id.sharemarket_recyclerview)
        activeRecyclerView.addItemDecoration(dividerItemDecoration)
        activeRecyclerView.layoutManager = LinearLayoutManager(this)
        container.addView(active)

        gainer = layoutInflater.inflate(R.layout.share_market_recycler_view, null)
        gainer.layoutParams = layoutParams
        gainer.visibility = View.GONE
        val gainerTitle = gainer.findViewById<TextView>(R.id.title)
        gainerTitle.text = "Most Gainer"
        val viewMostgainer = gainer.findViewById<TextView>(R.id.view_more)
        viewMostgainer.setOnClickListener {
            val intent = Intent(this@ShareMarket, CategoryShareMarket::class.java)
            intent.putExtra("title", gainerTitle.text.toString())
            intent.putExtra("list", gainerL)
            startActivity(intent)
        }
        gainerRecyclerView = gainer.findViewById(R.id.sharemarket_recyclerview)
        gainerRecyclerView.addItemDecoration(dividerItemDecoration)
        gainerRecyclerView.layoutManager = LinearLayoutManager(this)
        container.addView(gainer)

        loser = layoutInflater.inflate(R.layout.share_market_recycler_view, null)
        loser.layoutParams = layoutParams
        loser.visibility = View.GONE
        val loserTitle = loser.findViewById<TextView>(R.id.title)
        loserTitle.text = "Most Loser"
        val viewMostloser = loser.findViewById<TextView>(R.id.view_more)
        viewMostloser.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ShareMarket, CategoryShareMarket::class.java)
            intent.putExtra("title", loserTitle.text.toString())
            intent.putExtra("list", loserL)
            startActivity(intent)
        })
        loserRecyclerView = loser.findViewById(R.id.sharemarket_recyclerview)
        loserRecyclerView.addItemDecoration(dividerItemDecoration)
        loserRecyclerView.layoutManager = LinearLayoutManager(this)
        container.addView(loser)

        if (ConnectivityManager().checkConnectivity(this)) {

            progressBar.visibility = View.VISIBLE
            noData.text = null
            supportLoaderManager.initLoader(1, null, this)
            supportLoaderManager.initLoader(2, null, this)
            supportLoaderManager.initLoader(3, null, this)

        } else {
            progressBar.visibility = View.GONE
            noData.text = "No Internet Connection found!"
        }

        activeAdapter = CompanyAdapter(this, activeL)
        activeRecyclerView.setAdapter(activeAdapter)

        gainerAdapter = CompanyAdapter(this, gainerL)
        gainerRecyclerView.setAdapter(gainerAdapter)

        loserAdapter = CompanyAdapter(this, loserL)
        loserRecyclerView.setAdapter(loserAdapter)


    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Company>> {
        when (id) {
            1 -> return CompanyLoader(this, id)
            2 -> return CompanyLoader(this, id)
            3 -> return CompanyLoader(this, id)
            else -> return null as Loader<ArrayList<Company>>
        }
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Company>>, data: ArrayList<Company>?) {
        if (data!!.size !== 0) {
            progressBar.visibility = View.GONE
            noData.text = null

            if (data != null) {
                when (loader.getId()) {
                    1 -> {
                        active.visibility = View.VISIBLE
                        activeL.clear()
                        activeL.addAll(data)
                        activeAdapter.notifyDataSetChanged()
                    }
                    2 -> {
                        gainer.visibility = View.VISIBLE
                        gainerL.clear()
                        gainerL.addAll(data)
                        gainerAdapter.notifyDataSetChanged()
                    }
                    3 -> {
                        loser.visibility = View.VISIBLE
                        loserL.clear()
                        loserL.addAll(data)
                        loserAdapter.notifyDataSetChanged()
                    }
                    else -> {
                    }
                }
            }
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Company>>) {
        when (loader.getId()) {
            1 -> activeL.clear()
            2 -> gainerL.clear()
            3 -> loserL.clear()
            else -> {
            }
        }
    }

    private class CompanyLoader(context: Context, internal var mId: Int) :
        AsyncTaskLoader<ArrayList<Company>>(context) {
        override fun loadInBackground(): ArrayList<Company>? {
            return fetchCompanyData(mId)
        }

        override fun onStartLoading() {
            super.onStartLoading()
            forceLoad()
        }

        fun getid(): Int {
            return mId
        }

        fun fetchCompanyData(id: Int): ArrayList<Company> {

            val decimalFormat = DecimalFormat("#.###")
            val companyData = ArrayList<Company>()

            val url: URL
            var jsonObject: JSONObject? = null
            var jsonArray: JSONArray? = null
            Log.e("TAGG", "8")

            when (id) {
                1 -> {
                    url =
                        JSONUtils().createURL(Constants.ShareMarket.API_BASE_URL_STOCK + Constants.ShareMarket.API_MOST_ACTIVE)
                    println("URL is "+url)
                    jsonObject = null
                    try {
                        jsonObject = JSONObject(JSONUtils().getJsonString(url))
                        jsonArray = jsonObject.getJSONArray("mostActiveStock")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                2 -> {
                    url =
                        JSONUtils().createURL(Constants.ShareMarket.API_BASE_URL_STOCK + Constants.ShareMarket.API_MOST_GAINER)
                    println("URL is "+url)
                    try {
                        jsonObject = JSONObject(JSONUtils().getJsonString(url))
                        jsonArray = jsonObject.getJSONArray("mostGainerStock")


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                3 -> {
                    url =
                        JSONUtils().createURL(Constants.ShareMarket.API_BASE_URL_STOCK + Constants.ShareMarket.API_MOST_LOSER)
                    println("URL is "+url)
                    try {
                        jsonObject = JSONObject(JSONUtils().getJsonString(url))
                        jsonArray = jsonObject.getJSONArray("mostLoserStock")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                else -> {
                }
            }


            if (jsonArray != null) {

                for (i in 0 until jsonArray.length()) {

                    var `object`: JSONObject? = null
                    try {
                        `object` = jsonArray.getJSONObject(i)
                        val ticker = `object`!!.getString("ticker")
                        val changes = `object`.getDouble("changes")
                        val price = `object`.getDouble("price")
                        val percentChange = `object`.getString("changesPercentage")
                        val companyName = `object`.getString("companyName")

                        companyData.add(Company(ticker, companyName, price, percentChange, changes))

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }
            return companyData
        }
    }

    class CompanyAdapter(val context: Context, val companies: ArrayList<Company>) :
        RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

        var mContext: Context = context
        var mCompanies = companies

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.share_market_recyclerview_child, parent, false)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return Math.min(companies.size, 4)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val company = mCompanies[position]
            var mcompanyName: String? = null
            if (company.getCompanyName() != "null") {
                mcompanyName = company.getCompanyName()
            }
            val mvalue = company.getPrice()
            val mticker = company.getTicker()
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

            holder.companyName.setText(mcompanyName)
            holder.value.setText(mvalue.toString())
            holder.ticker.setText(mticker)
            holder.changePercent.setText(mchangePercent)
            holder.change.setText(mchange.toString())

            val McompanyName = mcompanyName
            holder.layout.setOnClickListener {
                val intent = Intent(mContext, SingleCompany::class.java)
                intent.putExtra("Symbol", mticker)
                intent.putExtra("CompanyName", McompanyName)
                mContext.startActivity(intent)
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var layout: LinearLayout = itemView as LinearLayout
            var companyName: TextView = itemView.findViewById(R.id.company_name)
            var value: TextView = itemView.findViewById(R.id.value)
            var ticker: TextView = itemView.findViewById(R.id.ticker)
            var changePercent: TextView = itemView.findViewById(R.id.change_percent)
            var change: TextView = itemView.findViewById(R.id.change)

        }
    }
}
