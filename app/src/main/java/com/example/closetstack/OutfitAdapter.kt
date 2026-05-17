package com.example.closetstack

import android.graphics.Color
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

    // Curated vibrant palette — each outfit gets 4 distinct colors
    private val colorPalette = listOf(
        listOf("#F5C842", "#E05C5C", "#5B9BD5", "#7DC47D"),
        listOf("#E8845A", "#6BAED6", "#F0C040", "#B07FD4"),
        listOf("#5BB5A2", "#E06080", "#F0A030", "#7098D4"),
        listOf("#D4736A", "#70A870", "#5080C8", "#E0C050"),
        listOf("#B06090", "#50A8A8", "#E09050", "#70B870"),
        listOf("#8070D0", "#D07050", "#50B090", "#D05070")
    )

    inner class OutfitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItem1: ImageView = view.findViewById(R.id.ivOutfitItem1)
        val ivItem2: ImageView = view.findViewById(R.id.ivOutfitItem2)
        val ivItem3: ImageView = view.findViewById(R.id.ivOutfitItem3)
        val ivItem4: ImageView = view.findViewById(R.id.ivOutfitItem4)
        val cellTopLeft: View = view.findViewById(R.id.cellTopLeft)
        val cellTopRight: View = view.findViewById(R.id.cellTopRight)
        val cellBottomLeft: View = view.findViewById(R.id.cellBottomLeft)
        val cellBottomRight: View = view.findViewById(R.id.cellBottomRight)
        val tvName: TextView = view.findViewById(R.id.tvOutfitName)
        val tvCategory: TextView = view.findViewById(R.id.tvOutfitCategory)
        val tvDesc: TextView = view.findViewById(R.id.tvOutfitDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_outfit_card, parent, false)
        return OutfitViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutfitViewHolder, position: Int) {
        val outfit = outfits[position]

        // Assign color set based on position (cycles through palette)
        val colors = colorPalette[position % colorPalette.size]
        holder.cellTopLeft.setBackgroundColor(Color.parseColor(colors[0]))
        holder.cellTopRight.setBackgroundColor(Color.parseColor(colors[1]))
        holder.cellBottomLeft.setBackgroundColor(Color.parseColor(colors[2]))
        holder.cellBottomRight.setBackgroundColor(Color.parseColor(colors[3]))

        holder.ivItem1.setImageResource(outfit.item1Res)
        holder.ivItem2.setImageResource(outfit.item2Res)
        holder.ivItem3.setImageResource(outfit.item3Res)
        holder.ivItem4.setImageResource(outfit.item4Res)

        holder.tvName.text = outfit.name
        holder.tvCategory.text = outfit.category.replaceFirstChar { it.uppercase() }
        holder.tvDesc.text = outfit.description.substringBefore("·").trim()

        holder.itemView.setOnClickListener { onOutfitClick(outfit) }
    }

    override fun getItemCount() = outfits.size

    fun updateOutfits(newOutfits: List<Outfit>) {
        outfits = newOutfits
        notifyDataSetChanged()
    }
}