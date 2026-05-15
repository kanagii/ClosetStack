package com.example.closetstack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class InspoItem(
    val imageRes: Int,
    val caption: String
)

class InspoAdapter(
    private val items: List<InspoItem>
) : RecyclerView.Adapter<InspoAdapter.InspoViewHolder>() {

    inner class InspoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivInspoImage: ImageView = view.findViewById(R.id.ivInspoImage)
        val tvInspoCaption: TextView = view.findViewById(R.id.tvInspoCaption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inspo_card, parent, false)
        return InspoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InspoViewHolder, position: Int) {
        val item = items[position]
        holder.ivInspoImage.setImageResource(item.imageRes)
        holder.tvInspoCaption.text = item.caption
    }

    override fun getItemCount() = items.size
}