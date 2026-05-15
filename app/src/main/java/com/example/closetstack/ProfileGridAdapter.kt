package com.example.closetstack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ProfileGridAdapter(
    private val imageResList: List<Int>,
    private val onItemClick: (Int) -> Unit = {}
) : RecyclerView.Adapter<ProfileGridAdapter.GridViewHolder>() {

    inner class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGridImage: ImageView = view.findViewById(R.id.ivGridImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_grid, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.ivGridImage.setImageResource(imageResList[position])
        holder.itemView.setOnClickListener { onItemClick(imageResList[position]) }
    }

    override fun getItemCount() = imageResList.size
}