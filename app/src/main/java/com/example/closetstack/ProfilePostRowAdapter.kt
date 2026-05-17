package com.example.closetstack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ProfilePostRowAdapter(
    private val images: List<Int>
) : RecyclerView.Adapter<ProfilePostRowAdapter.RowViewHolder>() {

    // Group images into rows of 3
    private val rows: List<List<Int>> = images.chunked(3).map { row ->
        // Pad with -1 if row has fewer than 3
        row + List(3 - row.size) { -1 }
    }

    inner class RowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv1: ImageView = view.findViewById(R.id.ivRowPost1)
        val iv2: ImageView = view.findViewById(R.id.ivRowPost2)
        val iv3: ImageView = view.findViewById(R.id.ivRowPost3)
        val tvRating1: TextView = view.findViewById(R.id.tvRating1)
        val tvRating2: TextView = view.findViewById(R.id.tvRating2)
        val tvRating3: TextView = view.findViewById(R.id.tvRating3)
        val ivMenu: ImageView = view.findViewById(R.id.ivRowMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_post_row, parent, false)
        return RowViewHolder(view)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val row = rows[position]
        val ivList = listOf(holder.iv1, holder.iv2, holder.iv3)
        val ratingList = listOf(holder.tvRating1, holder.tvRating2, holder.tvRating3)
        val ratings = listOf("4.5", "4.8", "4.2", "4.6", "4.9", "4.3", "4.7", "4.1", "4.4")

        row.forEachIndexed { i, res ->
            if (res != -1) {
                ivList[i].setImageResource(res)
                ivList[i].visibility = View.VISIBLE
                ratingList[i].text = ratings[(position * 3 + i) % ratings.size]
            } else {
                ivList[i].visibility = View.INVISIBLE
                ratingList[i].text = ""
            }
        }

        holder.ivMenu.setOnClickListener {
            Toast.makeText(it.context, "Post options coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = rows.size
}