package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClosetActivity : AppCompatActivity() {

    private lateinit var adapter: ClosetAdapter
    private lateinit var recyclerView: RecyclerView
    private var selectedCategory = "all"

    private val categories = listOf("all", "tops", "bottoms", "outer", "accessories", "shoes")

    private val allItems = listOf(
        ClothingItem("NY/121 Adjustable Cap", R.drawable.adjustable_cap, "accessories"),
        ClothingItem("BENCH Unisex Tee", R.drawable.benchtee, "tops"),
        ClothingItem("Bella Flowy Croptop", R.drawable.croptop, "tops"),
        ClothingItem("Taelor White Shirt", R.drawable.whiteshirt, "tops"),
        ClothingItem("Bello Flary Chino", R.drawable.flary_chino, "bottoms"),
        ClothingItem("UNIQLO Relaxed Pants", R.drawable.relaxedpants, "bottoms"),
        ClothingItem("GUCCI Monogram Jacket", R.drawable.guccijacket, "outer"),
        ClothingItem("SHEIN Plaid Print Skirt", R.drawable.plaidskirt, "bottoms"),
        ClothingItem("Sweettra Women's Blouse", R.drawable.womensblouse, "tops"),
        ClothingItem("Nike Air Force 1", R.drawable.airforce, "shoes")
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
                if (category != selectedCategory) selectCategory(category)
            }
        }
    }

    private fun selectCategory(newCategory: String) {
        val newIndex = categories.indexOf(newCategory)
        val oldIndex = categories.indexOf(selectedCategory)

        categoryViews[selectedCategory]?.apply {
            setBackgroundResource(R.drawable.bg_category_unselected)
            setTextColor(0xFFAAAAAA.toInt())
        }

        selectedCategory = newCategory
        categoryViews[selectedCategory]?.apply {
            setBackgroundResource(R.drawable.bg_category_selected)
            setTextColor(0xFF000000.toInt())
        }

        val filtered = if (newCategory == "all") allItems
        else allItems.filter { it.category == newCategory }

        recyclerView.startAnimation(
            AnimationUtils.loadAnimation(this, android.R.anim.fade_out).apply { duration = 150 }
        )

        recyclerView.postDelayed({
            adapter.updateItems(filtered)
            val anim = if (newIndex > oldIndex)
                AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
            else
                AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
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
        findViewById<android.view.View>(R.id.ivAddItem).setOnClickListener {
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
                        startActivity(Intent(this@ClosetActivity, OutfitsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_profile -> {
                        startActivity(Intent(this@ClosetActivity, ProfileActivity::class.java))
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