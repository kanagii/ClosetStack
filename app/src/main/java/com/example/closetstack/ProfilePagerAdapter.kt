package com.example.closetstack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class ProfilePagerAdapter(
    private val postImages: List<Int>,
    private val outfitImages: List<Int>,
    private val savedImages: List<Int>
) : RecyclerView.Adapter<ProfilePagerAdapter.PageViewHolder>() {

    inner class PageViewHolder(view: View, val recyclerView: RecyclerView) :
        RecyclerView.ViewHolder(view)

    override fun getItemCount() = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val rv = RecyclerView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutManager = GridLayoutManager(parent.context, 3)
        }
        return PageViewHolder(rv, rv)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val images = when (position) {
            0 -> postImages
            1 -> outfitImages
            else -> savedImages
        }
        holder.recyclerView.adapter = ProfileGridAdapter(images)
    }
}