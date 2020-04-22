package com.example.analytiq.adapter

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.analytiq.R
import com.example.analytiq.activity.Main2Activity
import com.example.analytiq.model.HomeListData
import java.util.logging.Handler
import java.util.zip.Inflater

class HomeAdapter(val context: Context, val list: ArrayList<HomeListData>,val act:Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_HEADER = 1
    val VIEW_ITEM = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.home_single_item, parent, false)
            return HomeViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.home_header, parent, false)
            return HeaderViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return VIEW_HEADER
        else
            return VIEW_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeViewHolder) {
            val item = list[position - 1]
            holder.name.text = item.name
            holder.img.setBackgroundResource(item.image)
            holder.back.setBackgroundResource(item.color)
            holder.card.setOnClickListener {
                val intent = Intent(context, Main2Activity::class.java)
                intent.putExtra("name", item.name)
                ObjectAnimator.ofFloat(holder.card, "translationX", -1100f).apply {
                    duration = 150
                    start()
                }
                android.os.Handler().postDelayed({
                    context.startActivity(intent)
                    act.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                },50)
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.bus_dep)
        val name = view.findViewById<TextView>(R.id.textView333)
        val back = view.findViewById<RelativeLayout>(R.id.home_single_item_rel)
        val card=view.findViewById<CardView>(R.id.home_card)
    }
}