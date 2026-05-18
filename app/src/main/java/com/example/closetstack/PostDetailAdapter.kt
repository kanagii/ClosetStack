package com.example.closetstack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostDetailAdapter(private val posts: List<Post>) :
    RecyclerView.Adapter<PostDetailAdapter.DetailViewHolder>() {

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivDetailImage: ImageView = view.findViewById(R.id.ivDetailImage)
        val tvDetailCaption: TextView = view.findViewById(R.id.tvDetailCaption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_detail, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val post = posts[position]
        holder.ivDetailImage.setImageResource(post.imageRes)
        holder.tvDetailCaption.text = "${post.username} — ${post.description}"
    }

    override fun getItemCount() = posts.size
}