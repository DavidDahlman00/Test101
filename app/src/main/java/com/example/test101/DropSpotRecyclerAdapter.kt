package com.example.test101

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DropSpotRecyclerAdapter(val context: Context, val dropSpots: List<DropItem>) :
    RecyclerView.Adapter<DropSpotRecyclerAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = layoutInflater.inflate(R.layout.dropspot_list_view, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dropSpots.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dropSpots = dropSpots[position]
        holder.textName.text = dropSpots.name
        holder.textLat.text = dropSpots.lat.toString()
        holder.textLng.text = dropSpots.lng.toString()
        holder.textCategory.text = dropSpots.category
        holder.textTime.text = dropSpots.time?.let { dropSpots.getDateTime(it) }

        holder.dropSpotPosition = position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.nameText)
        val textLat = itemView.findViewById<TextView>(R.id.latText)
        val textLng = itemView.findViewById<TextView>(R.id.lngText)
        val textCategory = itemView.findViewById<TextView>(R.id.categoryText)
        val textTime = itemView.findViewById<TextView>(R.id.timeText)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val delete = itemView.findViewById<ImageButton>(R.id.deleteButton)
        var dropSpotPosition = 0


       /* init{
            itemView.setOnClickListener{
                val intent = Intent(context, DropSpotActivity::class.java)
                intent.putExtra("dropSpotPositionKey", dropSpotPosition)
                context.startActivity(intent)
            }
        }*/
    }

}