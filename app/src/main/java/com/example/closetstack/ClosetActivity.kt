package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClosetActivity : AppCompatActivity() {

    private lateinit var adapter: ClosetAdapter
    private lateinit var recyclerView: RecyclerView
    private var selectedCategory = "all"
    private var previousCategoryIndex = 0

    private val categories = listOf("all", "tops", "bottoms", "outer", "accessories", "shoes")

    private val allItems = listOf(
        ClothingItem("NY/121 Adjustable Cap", R.drawable.img_post1, "accessories"),
        ClothingItem("BENCH Unisex Tee", R.drawable.img_post2, "tops"),
        ClothingItem("Bella Flowy Croptop", R.drawable.img_post3, "tops"),
        ClothingItem("Taelor White Shirt", R.drawable.img_post4, "tops"),
        ClothingItem("Bello Flary Chino", R.drawable.img_post5, "bottoms"),
        ClothingItem("UNIQLO Relaxed Pants", R.drawable.img_post6, "bottoms"),
        ClothingItem("GUCCI Monogram Jacket", R.drawable.img_post1, "outer"),
        ClothingItem("SHEIN Plaid Print Skirt", R.drawable.img_post2, "bottoms"),
        ClothingItem("Sweettra Women's Blouse", R.drawable.img_post3, "tops"),
        ClothingItem("Nike Air Force 1", R.drawable.img_post4, "shoes")
    )

    private val categoryViews: MutableMap<String, TextView> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet)

        setupCategoryViews()
        setupRecyclerView()
        setupBottomNav()
        setupAddButton()
    }

    private fun setupCategoryViews() {
        categoryViews["all"] = findViewById(R.id.tvCatAll)
        categoryViews["tops"] = findViewById(R.id.tvCatTops)
        categoryViews["bottoms"] = findViewById(R.id.tvCatBottoms)
        categoryViews["outer"] = findViewById(R.id.tvCatOuter)
        categoryViews["accessories"] = findViewById(R.id.tvCatAccessories)
        categoryViews["shoes"] = findViewById(R.id.tvCatShoes)

        categoryViews.forEach { (category, view) ->
            view.setOnClickListener {
                if (category != selectedCategory) {
                    selectCategory(category)
                }
            }
        }
    }

    private fun selectCategory(newCategory: String) {
        val newIndex = categories.indexOf(newCategory)
        val oldIndex = categories.indexOf(selectedCategory)

        // Slide direction: right if going forward in list, left if going back
        val slideIn = if (newIndex > oldIndex)
            android.R.anim.slide_in_left
        else
            android.R.anim.slide_out_right

        // Reset old tab style
        categoryViews[selectedCategory]?.apply {
            setBackgroundResource(R.drawable.bg_category_unselected)
            setTextColor(0xFFAAAAAA.toInt())
        }

        // Apply new tab style
        selectedCategory = newCategory
        categoryViews[selectedCategory]?.apply {
            setBackgroundResource(R.drawable.bg_category_selected)
            setTextColor(0xFF000000.toInt())
        }

        // Filter and animate
        val filtered = if (newCategory == "all") allItems
        else allItems.filter { it.category == newCategory }

        // Slide out current, update data, slide in new
        val slideOut = if (newIndex > oldIndex)
            android.R.anim.slide_out_right
        else
            android.R.anim.slide_in_left

        recyclerView.startAnimation(
            AnimationUtils.loadAnimation(this, android.R.anim.fade_out).apply {
                duration = 150
            }
        )

        recyclerView.postDelayed({
            adapter.updateItems(filtered)
            val anim = if (newIndex > oldIndex) {
                AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
            } else {
                AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            }
            recyclerView.startAnimation(anim)
        }, 150)
    }

    private fun setupRecyclerView() {
        adapter = ClosetAdapter(allItems)
        recyclerView = findViewById(R.id.rvClosetItems)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    private fun setupAddButton() {
        findViewById<android.widget.ImageView>(R.id.ivAddItem).setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    private fun setupBottomNav() {
        findViewById<BottomNavigationView>(R.id.bottomNav).apply {
            selectedItemId = R.id.nav_closet
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        startActivity(Intent(this@ClosetActivity, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_closet -> true
                    R.id.nav_outfits -> {
                        Toast.makeText(this@ClosetActivity, "Outfits coming soon!", Toast.LENGTH_SHORT).show()
                        false
                    }
                    R.id.nav_profile -> {
                        Toast.makeText(this@ClosetActivity, "Profile coming soon!", Toast.LENGTH_SHORT).show()
                        false
                    }
                    else -> false
                }
            }
        }
    }
}