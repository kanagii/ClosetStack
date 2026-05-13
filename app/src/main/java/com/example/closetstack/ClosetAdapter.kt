package com.example.closetstack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClosetAdapter(
    private var items: List<ClothingItem>
) : RecyclerView.Adapter<ClosetAdapter.ClosetViewHolder>() {

    inner class ClosetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivClothingImage: ImageView = view.findViewById(R.id.ivClothingImage)
        val tvClothingName: TextView = view.findViewById(R.id.tvClothingName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_closet_clothing, parent, false)
        return ClosetViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClosetViewHolder, position: Int) {
        val item = items[position]
        holder.ivClothingImage.setImageResource(item.imageRes)
        holder.tvClothingName.text = item.name
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<ClothingItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}