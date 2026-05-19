package com.example.closetstack

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: PostAdapter
    private var currentTab = "all"

    private val allPosts = listOf(
        Post(username = "Justin Nabunturan", description = "Lakers in game 2", caption = "Average Rating: 4.5", imageRes = R.drawable.img_post1, avatarRes = R.drawable.img_user1, timestamp = "2h ago", feedType = "all"),
        Post(username = "Jerome Batumbakal", description = "Street core fits only", caption = "Average Rating: 4.8", imageRes = R.drawable.img_post2, avatarRes = R.drawable.img_user2, timestamp = "4h ago", feedType = "all"),
        Post(username = "Adam Marc", description = "Minimalist Monday", caption = "Average Rating: 4.4", imageRes = R.drawable.img_post3, avatarRes = R.drawable.img_user4, timestamp = "6h ago", feedType = "following"),
        Post(username = "Onse Fernandez", description = "Vintage thrift haul 🧥", caption = "Average Rating: 4.9", imageRes = R.drawable.img_post4, avatarRes = R.drawable.img_user3, timestamp = "8h ago", feedType = "follower"),
        Post(username = "bipoygaming", description = "kinsa nakakita ani bayhana tawage ko salamt", caption = "Average Rating: 4.2", imageRes = R.drawable.img_post5, avatarRes = R.drawable.usertop2, timestamp = "10h ago", feedType = "following"),
        Post(username = "strt_apoy", description = "jacket ta gamay bisag init pilipinas", caption = "Average Rating: 4.8", imageRes = R.drawable.img_post6, avatarRes = R.drawable.usertop1, timestamp = "12h ago", feedType = "all"),
        Post(username = "AyanoKoji", description = "Clean fit check 🖤", caption = "Average Rating: 4.6", imageRes = R.drawable.img_post1, avatarRes = R.drawable.usertop4, timestamp = "14h ago", feedType = "follower"),
        Post(username = "Erosyonz", description = "Layering season is here", caption = "Average Rating: 4.3", imageRes = R.drawable.img_post2, avatarRes = R.drawable.usertop5, timestamp = "16h ago", feedType = "all"),
        Post(username = "Andre Pham", description = "New cop just dropped 🔥", caption = "Average Rating: 4.7", imageRes = R.drawable.img_post3, avatarRes = R.drawable.usertop6, timestamp = "18h ago", feedType = "following"),
        Post(username = "boyHipak29", description = "Old money aesthetic today", caption = "Average Rating: 4.5", imageRes = R.drawable.img_post4, avatarRes = R.drawable.usertop3, timestamp = "20h ago", feedType = "follower"),
        Post(username = "Justin Nabunturan", description = "Second fit of the week", caption = "Average Rating: 4.9", imageRes = R.drawable.img_post5, avatarRes = R.drawable.img_user1, timestamp = "1d ago", feedType = "following"),
        Post(username = "Jerome Batumbakal", description = "Oversized everything 💯", caption = "Average Rating: 4.1", imageRes = R.drawable.img_post6, avatarRes = R.drawable.img_user2, timestamp = "1d ago", feedType = "all")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupPostsFeed()
        setupFeedTabs()
        BottomNavManager.setup(this, NavScreen.HOME)
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.updatePosts(getFilteredPosts(currentTab))
        }
    }

    private fun setupPostsFeed() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvPosts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        PostRepository.posts = allPosts  // ADD THIS

        adapter = PostAdapter(
            getFilteredPosts("all"),
            onSaveClick = { SaveToBoardBottomSheet().show(supportFragmentManager, "SaveToBoard") },
            onRatingChanged = { post, rating ->
                Toast.makeText(this, "You rated ${post.username}'s fit $rating ⭐", Toast.LENGTH_SHORT).show()
            },
//            onPostClick = { _, position ->           // ADD THIS
//                val intent = android.content.Intent(this, PostDetailActivity::class.java)
//                intent.putExtra("position", position)
//                startActivity(intent)
//            }

                    onPostClick = { post, position ->
                if (post.avatarRes == R.drawable.img_user1) {
                    val intent = android.content.Intent(this, ProfileActivityTest::class.java)
                    startActivity(intent)
                } else {
                    val intent = android.content.Intent(this, PostDetailActivity::class.java)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupFeedTabs() {
        val tvFollowing = findViewById<TextView>(R.id.tvFollowing)
        val tvAll = findViewById<TextView>(R.id.tvAll)
        val tvFollower = findViewById<TextView>(R.id.tvFollower)

        fun selectTab(tab: String) {
            currentTab = tab
            listOf(tvFollowing, tvAll, tvFollower).forEach {
                it.setTextColor(0xFF888888.toInt())
                it.paint.isFakeBoldText = false
            }
            val selected = when (tab) {
                "following" -> tvFollowing
                "follower" -> tvFollower
                else -> tvAll
            }
            selected.setTextColor(0xFFFFFFFF.toInt())
            selected.paint.isFakeBoldText = true
            adapter.updatePosts(getFilteredPosts(tab))
        }

        tvFollowing.setOnClickListener { selectTab("following") }
        tvAll.setOnClickListener { selectTab("all") }
        tvFollower.setOnClickListener { selectTab("follower") }
        selectTab("all")
    }

    private fun getFilteredPosts(tab: String): List<Post> {
        val combined = PostRepository.getUserPosts() + allPosts
        return if (tab == "all") combined else combined.filter { it.feedType == tab }
    }
}