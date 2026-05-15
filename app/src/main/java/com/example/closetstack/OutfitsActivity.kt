package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class OutfitsActivity : AppCompatActivity() {

    private lateinit var adapter: OutfitAdapter
    private var selectedCategory = "all"

    private val categories = listOf("all", "casual", "formal", "business", "streetwear")

    private val allOutfits = listOf(
        Outfit("Hype Boy", "4 items · casual", "casual", R.drawable.img_post1, R.drawable.img_post2, R.drawable.img_post3, R.drawable.img_post4),
        Outfit("Oversized Feels", "4 items · streetwear", "streetwear", R.drawable.img_post5, R.drawable.img_post6, R.drawable.img_post1, R.drawable.img_post2),
        Outfit("Saturday", "3 items · casual", "casual", R.drawable.img_post3, R.drawable.img_post4, R.drawable.img_post5, R.drawable.img_post6),
        Outfit("Office Ready", "4 items · formal", "formal", R.drawable.img_post2, R.drawable.img_post3, R.drawable.img_post4, R.drawable.img_post5),
        Outfit("Street King", "4 items · streetwear", "streetwear", R.drawable.img_post6, R.drawable.img_post1, R.drawable.img_post2, R.drawable.img_post3),
        Outfit("Boardroom", "4 items · business", "business", R.drawable.img_post4, R.drawable.img_post5, R.drawable.img_post6, R.drawable.img_post1)
    )

    private val categoryViews: MutableMap<String, TextView> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfits)

        setupCategoryViews()
        setupRecyclerView()
        setupAddButton()
        setupBottomNav()
    }

    private fun setupCategoryViews() {
        categoryViews["all"] = findViewById(R.id.tvOCatAll)
        categoryViews["casual"] = findViewById(R.id.tvOCatCasual)
        categoryViews["formal"] = findViewById(R.id.tvOCatFormal)
        categoryViews["business"] = findViewById(R.id.tvOCatBusiness)
        categoryViews["streetwear"] = findViewById(R.id.tvOCatStreet)

        categoryViews.forEach { (category, view) ->
            view.setOnClickListener {
                if (category != selectedCategory) selectCategory(category)
            }
        }
    }

    private fun selectCategory(category: String) {
        categoryViews[selectedCategory]?.apply {
            setBackgroundResource(R.drawable.bg_category_unselected)
            setTextColor(0xFFAAAAAA.toInt())
        }
        selectedCategory = category
        categoryViews[selectedCategory]?.apply {
            setBackgroundResource(R.drawable.bg_category_selected)
            setTextColor(0xFF000000.toInt())
        }
        val filtered = if (category == "all") allOutfits
        else allOutfits.filter { it.category == category }
        adapter.updateOutfits(filtered)
    }

    private fun setupRecyclerView() {
        adapter = OutfitAdapter(allOutfits) { outfit ->
            // Open Style Canvas for this outfit
            startActivity(Intent(this, StyleCanvasActivity::class.java))
            overridePendingTransition(0, 0)
        }
        val rv = findViewById<RecyclerView>(R.id.rvOutfits)
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = adapter
    }

    private fun setupAddButton() {
        findViewById<android.widget.ImageView>(R.id.ivAddOutfit).setOnClickListener {
            startActivity(Intent(this, StyleCanvasActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    private fun setupBottomNav() {
        findViewById<BottomNavigationView>(R.id.bottomNav).apply {
            selectedItemId = R.id.nav_outfits
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        startActivity(Intent(this@OutfitsActivity, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_closet -> {
                        startActivity(Intent(this@OutfitsActivity, ClosetActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_outfits -> true
                    R.id.nav_profile -> {
                        startActivity(Intent(this@OutfitsActivity, ProfileActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}