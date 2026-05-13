package com.example.closetstack

import android.content.Intent
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
                username = "Justin Nabunturan",
                description = "Lakers in game 2",
                caption = "Average Rating: 4.5",
                imageRes = R.drawable.img_post1,
                avatarRes = R.drawable.img_user1,
                timestamp = "2h ago"
            ),
            Post(
                username = "Jerome Batumbakal",
                description = "Street core fits only",
                caption = "Average Rating: 4.8",
                imageRes = R.drawable.img_post2,
                avatarRes = R.drawable.img_user2,
                timestamp = "4h ago"
            ),
            Post(
                username = "Adam Marc",
                description = "Minimalist Monday",
                caption = "Average Rating: 4.4",
                imageRes = R.drawable.img_post3,
                avatarRes = R.drawable.img_user4,
                timestamp = "6h ago"
            ),
            Post(
                username = "Onse Fernandez",
                description = "Vintage thrift haul 🧥",
                caption = "Average Rating: 4.9",
                imageRes = R.drawable.img_post4,
                avatarRes = R.drawable.img_user3,
                timestamp = "8h ago"
            ),
            Post(
                username = "bipoygaming",
                description = "kinsa nakakita ani bayhana tawage ko salamt",
                caption = "Average Rating: 4.2",
                imageRes = R.drawable.img_post5,
                avatarRes = R.drawable.usertop2,
                timestamp = "10h ago"
            ),
            Post(
                username = "strt_apoy",
                description = "jacket ta gamay bisag init pilipinas",
                caption = "Average Rating: 4.8",
                imageRes = R.drawable.img_post6,
                avatarRes = R.drawable.usertop1,
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
                        startActivity(Intent(this, ClosetActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
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