package com.example.analytiq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.analytiq.R
import com.example.analytiq.model.TeamListData

class TeamAdapter(val list:ArrayList<TeamListData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==0){
            val view=LayoutInflater.from(parent.context).inflate(R.layout.team_header,parent,false)
            return TeamHeaderViewHolder(view)
        }else{
            val view=LayoutInflater.from(parent.context).inflate(R.layout.team_single_row,parent,false)
            return TeamViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0)
            return 0
        return 1
    }

    override fun getItemCount(): Int {
        return list.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TeamViewHolder){
            val item=list[position-1]
            holder.name.text=item.name
            holder.desc.text=item.desc
            holder.image.setImageResource(item.image)
        }
    }

    class TeamHeaderViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    class TeamViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image=view.findViewById<ImageView>(R.id.imageView1)
        val name=view.findViewById<TextView>(R.id.textView11)
        val desc=view.findViewById<TextView>(R.id.editText2)
    }
}