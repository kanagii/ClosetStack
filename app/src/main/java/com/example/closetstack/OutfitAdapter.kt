package com.example.closetstack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OutfitAdapter(
    private var outfits: List<Outfit>,
    private val onOutfitClick: (Outfit) -> Unit
) : RecyclerView.Adapter<OutfitAdapter.OutfitViewHolder>() {

    inner class OutfitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItem1: ImageView = view.findViewById(R.id.ivOutfitItem1)
        val ivItem2: ImageView = view.findViewById(R.id.ivOutfitItem2)
        val ivItem3: ImageView = view.findViewById(R.id.ivOutfitItem3)
        val ivItem4: ImageView = view.findViewById(R.id.ivOutfitItem4)
        val tvName: TextView = view.findViewById(R.id.tvOutfitName)
        val tvDesc: TextView = view.findViewById(R.id.tvOutfitDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_outfit_card, parent, false)
        return OutfitViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutfitViewHolder, position: Int) {
        val outfit = outfits[position]
        holder.ivItem1.setImageResource(outfit.item1Res)
        holder.ivItem2.setImageResource(outfit.item2Res)
        holder.ivItem3.setImageResource(outfit.item3Res)
        holder.ivItem4.setImageResource(outfit.item4Res)
        holder.tvName.text = outfit.name
        holder.tvDesc.text = outfit.description
        holder.itemView.setOnClickListener { onOutfitClick(outfit) }
    }

    override fun getItemCount() = outfits.size

    fun updateOutfits(newOutfits: List<Outfit>) {
        outfits = newOutfits
        notifyDataSetChanged()
    }
}