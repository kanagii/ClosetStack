package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class OutfitsActivity : AppCompatActivity() {

    private lateinit var adapter: OutfitAdapter
    private var selectedCategory = "all"

    private val categories = listOf("all", "casual", "formal", "business", "streetwear")

    // Mutable list so new outfits can be added
    private val allOutfits = mutableListOf(
        Outfit("Street Cap Fit", "4 items · streetwear", "streetwear", R.drawable.adjustable_cap, R.drawable.benchtee, R.drawable.relaxedpants, R.drawable.airforce),
        Outfit("Crop & Chino", "4 items · casual", "casual", R.drawable.croptop, R.drawable.flary_chino, R.drawable.airforce, R.drawable.adjustable_cap),
        Outfit("Office Ready", "4 items · formal", "formal", R.drawable.whiteshirt, R.drawable.relaxedpants, R.drawable.guccijacket, R.drawable.airforce),
        Outfit("Gucci Street", "4 items · streetwear", "streetwear", R.drawable.guccijacket, R.drawable.benchtee, R.drawable.plaidskirt, R.drawable.airforce),
        Outfit("Blouse & Plaid", "4 items · casual", "casual", R.drawable.womensblouse, R.drawable.plaidskirt, R.drawable.airforce, R.drawable.adjustable_cap),
        Outfit("Boardroom", "4 items · business", "business", R.drawable.whiteshirt, R.drawable.guccijacket, R.drawable.relaxedpants, R.drawable.airforce)
    )

    private val categoryViews: MutableMap<String, TextView> = mutableMapOf()

    // Launcher that receives new outfit back from StyleCanvasActivity
    private val styleCanvasLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult

            val name     = data.getStringExtra("outfit_name") ?: return@registerForActivityResult
            val category = data.getStringExtra("outfit_category") ?: "casual"
            val item1    = data.getIntExtra("outfit_item1", R.drawable.benchtee)
            val item2    = data.getIntExtra("outfit_item2", R.drawable.relaxedpants)
            val item3    = data.getIntExtra("outfit_item3", R.drawable.airforce)
            val item4    = data.getIntExtra("outfit_item4", R.drawable.adjustable_cap)

            val newOutfit = Outfit(
                name = name,
                description = "4 items · $category",
                category = category,
                item1Res = item1,
                item2Res = item2,
                item3Res = item3,
                item4Res = item4
            )

            allOutfits.add(0, newOutfit) // Add to top
            refreshList()
            Toast.makeText(this, "\"$name\" added to your outfits!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfits)

        setupCategoryViews()
        setupRecyclerView()
        setupAddButton()
        setupBottomNav()
    }

    private fun setupCategoryViews() {
        categoryViews["all"]        = findViewById(R.id.tvOCatAll)
        categoryViews["casual"]     = findViewById(R.id.tvOCatCasual)
        categoryViews["formal"]     = findViewById(R.id.tvOCatFormal)
        categoryViews["business"]   = findViewById(R.id.tvOCatBusiness)
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
        refreshList()
    }

    private fun refreshList() {
        val filtered = if (selectedCategory == "all") allOutfits
        else allOutfits.filter { it.category == selectedCategory }
        adapter.updateOutfits(filtered)

        // Update stats
        findViewById<TextView>(R.id.tvOutfitStats).text = "${allOutfits.size} outfits"
    }

    private fun setupRecyclerView() {
        adapter = OutfitAdapter(allOutfits) { _ ->
            Toast.makeText(this, "Outfit editing coming soon!", Toast.LENGTH_SHORT).show()
        }
        val rv = findViewById<RecyclerView>(R.id.rvOutfits)
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = adapter
    }

    private fun setupAddButton() {
        findViewById<android.view.View>(R.id.ivAddOutfit).setOnClickListener {
            styleCanvasLauncher.launch(
                Intent(this, StyleCanvasActivity::class.java)
            )
            overridePendingTransition(0, 0)
        }
    }

    private fun setupBottomNav() {
        BottomNavManager.setup(this, NavScreen.OUTFITS)
    }


}