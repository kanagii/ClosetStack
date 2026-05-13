package com.example.closetstack

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupPostsFeed()
        setupFab()
        setupBottomNav()
    }

    private fun setupPostsFeed() {
        val posts = listOf(
            Post(
                username = "Justin Nabuniuan",
                description = "Lakers in game 2",
                caption = "Autumn Layering 2025",
                imageRes = R.drawable.img_post1,
                avatarRes = R.drawable.img_user1,
                timestamp = "2h ago"
            ),
            Post(
                username = "Jerome Batumbakal",
                description = "Street core fits only",
                caption = "Street Ready",
                imageRes = R.drawable.img_post2,
                avatarRes = R.drawable.img_user2,
                timestamp = "4h ago"
            ),
            Post(
                username = "Adam Marc",
                description = "Minimalist Monday",
                caption = "Less is More",
                imageRes = R.drawable.img_post3,
                avatarRes = R.drawable.img_user3,
                timestamp = "6h ago"
            ),
            Post(
                username = "KyleRoss",
                description = "Vintage thrift haul 🧥",
                caption = "Thrift King",
                imageRes = R.drawable.img_post4,
                avatarRes = R.drawable.img_user4,
                timestamp = "8h ago"
            ),
            Post(
                username = "shoppingg",
                description = "New season, new me 🍂",
                caption = "Fall Fits 2025",
                imageRes = R.drawable.img_post5,
                avatarRes = R.drawable.img_user5,
                timestamp = "10h ago"
            ),
            Post(
                username = "DropfitBCS",
                description = "Drip or drown 💧",
                caption = "Full Send Fit",
                imageRes = R.drawable.img_post6,
                avatarRes = R.drawable.img_user6,
                timestamp = "12h ago"
            )
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rvPosts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PostAdapter(posts,
            onSaveClick = { post ->
                Toast.makeText(this, "Save feature coming soon!", Toast.LENGTH_SHORT).show()
            },
            onRatingChanged = { post, rating ->
                Toast.makeText(this, "You rated ${post.username}'s fit $rating ⭐", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupFab() {
        Toast.makeText(this, "Post feature coming soon!", Toast.LENGTH_SHORT).show()
    }

    private fun setupBottomNav() {
        findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
            .setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true
                    R.id.nav_closet -> {
                        Toast.makeText(this, "Closet screen coming soon!", Toast.LENGTH_SHORT).show()
                        false
                    }
                    R.id.nav_outfits -> {
                        Toast.makeText(this, "Outfits screen coming soon!", Toast.LENGTH_SHORT).show()
                        false
                    }
                    R.id.nav_profile -> {
                        Toast.makeText(this, "Profile screen coming soon!", Toast.LENGTH_SHORT).show()
                        false
                    }
                    else -> false
                }
            }
    }
}