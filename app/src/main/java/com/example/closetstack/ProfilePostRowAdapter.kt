package com.example.closetstack

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

// Wrapper for either a drawable resource or a URI string
sealed class ProfilePostImage {
    data class Res(val resId: Int) : ProfilePostImage()
    data class Uri(val uri: String) : ProfilePostImage()
    object Empty : ProfilePostImage()
}

class ProfilePostRowAdapter(
    private val images: List<ProfilePostImage>
) : RecyclerView.Adapter<ProfilePostRowAdapter.RowViewHolder>() {

    private val rows: List<List<ProfilePostImage>> = images.chunked(3).map { row ->
        row + List(3 - row.size) { ProfilePostImage.Empty }
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
        val seededRatings = listOf("4.5", "4.8", "4.2", "4.6", "4.9", "4.3", "4.7", "4.1", "4.4")

        row.forEachIndexed { i, img ->
            when (img) {
                is ProfilePostImage.Res -> {
                    ivList[i].setImageResource(img.resId)
                    ivList[i].visibility = View.VISIBLE
                    ratingList[i].text = seededRatings[(position * 3 + i) % seededRatings.size]
                }
                is ProfilePostImage.Uri -> {
                    ivList[i].setImageURI(Uri.parse(img.uri))
                    ivList[i].visibility = View.VISIBLE
                    ratingList[i].text = "0.0"
                }
                ProfilePostImage.Empty -> {
                    ivList[i].visibility = View.INVISIBLE
                    ratingList[i].text = ""
                }
            }
        }

        holder.ivMenu.setOnClickListener {
            Toast.makeText(it.context, "Post options coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = rows.size
}