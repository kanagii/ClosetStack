package com.example.closetstack

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private var posts: List<Post>,
    private val onSaveClick: (Post) -> Unit,
    private val onRatingChanged: (Post, Float) -> Unit,
    private val onPostClick: (Post, Int) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(){

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPostImage: ImageView = view.findViewById(R.id.ivPostImage)
        val ivUserAvatar: ImageView = view.findViewById(R.id.ivUserAvatar)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvDescription: TextView = view.findViewById(R.id.tvPostDescription)
        val tvCaption: TextView = view.findViewById(R.id.tvPostCaption)
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val ivSave: ImageView = view.findViewById(R.id.ivSave)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val tvRatingCount: TextView = view.findViewById(R.id.tvRatingCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        if (post.imageUri != null) {
            holder.ivPostImage.setImageURI(android.net.Uri.parse(post.imageUri))
        } else {
            holder.ivPostImage.setImageResource(post.imageRes)
        }
        holder.ivUserAvatar.setImageResource(post.avatarRes)
        holder.tvUsername.text = post.username
        holder.tvDescription.text = post.description
        holder.tvCaption.text = post.caption
        holder.tvTimestamp.text = post.timestamp
        holder.ratingBar.rating = post.userRating

        // Update save icon state
        updateSaveIcon(holder.ivSave, post.isSaved)

        // Save toggle
        holder.ivSave.setOnClickListener {
            post.isSaved = !post.isSaved
            updateSaveIcon(holder.ivSave, post.isSaved)
            onSaveClick(post)
        }

        // Rating
        holder.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, fromUser ->
                if (fromUser) {
                    post.userRating = rating
                    holder.tvRatingCount.text = "(${rating.toInt()})"
                    onRatingChanged(post, rating)
                }
            }

        holder.ratingBar.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }

        holder.ivPostImage.setOnClickListener {
            val intent = android.content.Intent(holder.itemView.context, PostDetailActivity::class.java)
            intent.putExtra("imageRes", post.imageRes)
            holder.itemView.context.startActivity(intent)
        }
    }

    private fun updateSaveIcon(ivSave: ImageView, isSaved: Boolean) {
        if (isSaved) {
            ivSave.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            ivSave.setImageResource(R.drawable.ic_bookmark_outline)
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}