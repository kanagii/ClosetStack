package com.example.closetstack

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class StyleCanvasActivity : BaseActivity() {

    private var slotTopRes: Int = R.drawable.benchtee
    private var slotBottomRes: Int = R.drawable.relaxedpants
    private var slotShoesRes: Int = R.drawable.airforce
    private var slotAccessoriesRes: Int? = null

    private enum class ActiveSlot { TOP, BOTTOM, SHOES, ACCESSORIES }
    private var activeSlot = ActiveSlot.TOP

    private var selectedCategory = "casual"

    // Defines which closet categories are allowed per slot
    private val slotAllowedCategories = mapOf(
        ActiveSlot.TOP         to listOf("tops", "outer"),
        ActiveSlot.BOTTOM      to listOf("bottoms"),
        ActiveSlot.SHOES       to listOf("shoes"),
        ActiveSlot.ACCESSORIES to listOf("accessories")
    )

    private val allClosetItems = listOf(
        ClothingItem("NY/121 Adjustable Cap", R.drawable.adjustable_cap, "accessories"),
        ClothingItem("BENCH Unisex Tee", R.drawable.benchtee, "tops"),
        ClothingItem("Bella Flowy Croptop", R.drawable.croptop, "tops"),
        ClothingItem("Taelor White Shirt", R.drawable.whiteshirt, "tops"),
        ClothingItem("Bello Flary Chino", R.drawable.flary_chino, "bottoms"),
        ClothingItem("UNIQLO Relaxed Pants", R.drawable.relaxedpants, "bottoms"),
        ClothingItem("GUCCI Monogram Jacket", R.drawable.guccijacket, "outer"),
        ClothingItem("SHEIN Plaid Skirt", R.drawable.plaidskirt, "bottoms"),
        ClothingItem("Sweettra Women's Blouse", R.drawable.womensblouse, "tops"),
        ClothingItem("Nike Air Force 1", R.drawable.airforce, "shoes")
    )

    private lateinit var closetAdapter: ClosetAdapter
    private var filteredItems = allClosetItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style_canvas)

        setupBack()
        setupCategorySelector()
        setupSlots()
        setupAiSuggestions()
        setupClosetFilter()
        setupClosetGrid()
        setupSaveReset()

        // Start with TOP active and filter to tops
        setActiveSlot(ActiveSlot.TOP)
        applySlotFilter(ActiveSlot.TOP)
    }

    private fun setupBack() {
        findViewById<View>(R.id.ivBack).setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun setupCategorySelector() {
        val catViews = mapOf(
            "casual"     to R.id.tvCanvasCatCasual,
            "formal"     to R.id.tvCanvasCatFormal,
            "business"   to R.id.tvCanvasCatBusiness,
            "streetwear" to R.id.tvCanvasCatStreet
        )

        catViews.forEach { (cat, id) ->
            findViewById<TextView>(id).setOnClickListener {
                catViews.values.forEach { viewId ->
                    findViewById<TextView>(viewId).apply {
                        setBackgroundResource(R.drawable.bg_category_unselected)
                        setTextColor(0xFFAAAAAA.toInt())
                    }
                }
                selectedCategory = cat
                (it as TextView).apply {
                    setBackgroundResource(R.drawable.bg_category_selected)
                    setTextColor(0xFF000000.toInt())
                }
            }
        }
    }

    private fun setupSlots() {
        findViewById<View>(R.id.slotTop).setOnClickListener {
            setActiveSlot(ActiveSlot.TOP)
            applySlotFilter(ActiveSlot.TOP)
        }
        findViewById<View>(R.id.slotBottom).setOnClickListener {
            setActiveSlot(ActiveSlot.BOTTOM)
            applySlotFilter(ActiveSlot.BOTTOM)
        }
        findViewById<View>(R.id.slotShoes).setOnClickListener {
            setActiveSlot(ActiveSlot.SHOES)
            applySlotFilter(ActiveSlot.SHOES)
        }
        findViewById<View>(R.id.slotAccessories).setOnClickListener {
            setActiveSlot(ActiveSlot.ACCESSORIES)
            applySlotFilter(ActiveSlot.ACCESSORIES)
        }
    }

    private fun setupAiSuggestions() {
        findViewById<View>(R.id.btnAiSuggestTop).setOnClickListener {
            suggestItemForSlot(ActiveSlot.TOP)
        }
        findViewById<View>(R.id.btnAiSuggestBottom).setOnClickListener {
            suggestItemForSlot(ActiveSlot.BOTTOM)
        }
        findViewById<View>(R.id.btnAiSuggestShoes).setOnClickListener {
            suggestItemForSlot(ActiveSlot.SHOES)
        }
        findViewById<View>(R.id.btnAiSuggestAccessories).setOnClickListener {
            suggestItemForSlot(ActiveSlot.ACCESSORIES)
        }
    }

    private fun suggestItemForSlot(slot: ActiveSlot) {
        // 1. Get the allowed categories for this slot
        val allowed = slotAllowedCategories[slot] ?: return

        // 2. Filter our closet items to only those that fit
        val possibleItems = allClosetItems.filter { it.category in allowed }

        // 3. Pick a random item (Mocking the AI suggestion)
        if (possibleItems.isNotEmpty()) {
            val aiSuggestedItem = possibleItems.random()

            // 4. Update the UI directly
            when (slot) {
                ActiveSlot.TOP -> {
                    slotTopRes = aiSuggestedItem.imageRes
                    findViewById<ImageView>(R.id.ivSlotTop).setImageResource(aiSuggestedItem.imageRes)
                }
                ActiveSlot.BOTTOM -> {
                    slotBottomRes = aiSuggestedItem.imageRes
                    findViewById<ImageView>(R.id.ivSlotBottom).setImageResource(aiSuggestedItem.imageRes)
                }
                ActiveSlot.SHOES -> {
                    slotShoesRes = aiSuggestedItem.imageRes
                    findViewById<ImageView>(R.id.ivSlotShoes).setImageResource(aiSuggestedItem.imageRes)
                }
                ActiveSlot.ACCESSORIES -> {
                    slotAccessoriesRes = aiSuggestedItem.imageRes
                    val iv = findViewById<ImageView>(R.id.ivSlotAccessories)
                    iv.setImageResource(aiSuggestedItem.imageRes)
                    iv.visibility = View.VISIBLE
                    findViewById<View>(R.id.layoutAccessoriesEmpty).visibility = View.GONE
                }
            }

            // Optionally set this slot as active so the closet grid below shows the category
            setActiveSlot(slot)
            applySlotFilter(slot)

            Toast.makeText(this, "AI suggestion applied!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No items available in closet for this slot.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setActiveSlot(slot: ActiveSlot) {
        activeSlot = slot

        val ringMap = mapOf(
            ActiveSlot.TOP         to R.id.ringSlotTop,
            ActiveSlot.BOTTOM      to R.id.ringSlotBottom,
            ActiveSlot.SHOES       to R.id.ringSlotShoes,
            ActiveSlot.ACCESSORIES to R.id.ringSlotAccessories
        )
        ringMap.forEach { (s, id) ->
            findViewById<View>(id).visibility =
                if (s == slot) View.VISIBLE else View.GONE
        }

        val alphaMap = mapOf(
            ActiveSlot.TOP         to R.id.slotTop,
            ActiveSlot.BOTTOM      to R.id.slotBottom,
            ActiveSlot.SHOES       to R.id.slotShoes,
            ActiveSlot.ACCESSORIES to R.id.slotAccessories
        )
        alphaMap.forEach { (s, id) ->
            findViewById<View>(id).alpha = if (s == slot) 1f else 0.55f
        }
    }

    // Filters the closet grid to only show items valid for the active slot
    private fun applySlotFilter(slot: ActiveSlot) {
        val allowed = slotAllowedCategories[slot] ?: listOf()
        filteredItems = allClosetItems.filter { it.category in allowed }
        closetAdapter.updateItems(filteredItems)

        // Update filter tab UI to reflect the slot's category
        val displayCat = allowed.firstOrNull() ?: "all"
        updateFilterUI(displayCat)
    }

    private fun fillActiveSlot(item: ClothingItem) {
        // Validate category against allowed for this slot
        val allowed = slotAllowedCategories[activeSlot] ?: listOf()
        if (item.category !in allowed) {
            val allowedStr = allowed.joinToString(" or ")
            Toast.makeText(
                this,
                "This slot only accepts $allowedStr items",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        when (activeSlot) {
            ActiveSlot.TOP -> {
                slotTopRes = item.imageRes
                findViewById<ImageView>(R.id.ivSlotTop).setImageResource(item.imageRes)
            }
            ActiveSlot.BOTTOM -> {
                slotBottomRes = item.imageRes
                findViewById<ImageView>(R.id.ivSlotBottom).setImageResource(item.imageRes)
            }
            ActiveSlot.SHOES -> {
                slotShoesRes = item.imageRes
                findViewById<ImageView>(R.id.ivSlotShoes).setImageResource(item.imageRes)
            }
            ActiveSlot.ACCESSORIES -> {
                slotAccessoriesRes = item.imageRes
                val iv = findViewById<ImageView>(R.id.ivSlotAccessories)
                iv.setImageResource(item.imageRes)
                iv.visibility = View.VISIBLE
                findViewById<View>(R.id.layoutAccessoriesEmpty).visibility = View.GONE
            }
        }

        // Auto-advance to next slot
        when (activeSlot) {
            ActiveSlot.TOP -> {
                setActiveSlot(ActiveSlot.BOTTOM)
                applySlotFilter(ActiveSlot.BOTTOM)
            }
            ActiveSlot.BOTTOM -> {
                setActiveSlot(ActiveSlot.SHOES)
                applySlotFilter(ActiveSlot.SHOES)
            }
            ActiveSlot.SHOES -> {
                setActiveSlot(ActiveSlot.ACCESSORIES)
                applySlotFilter(ActiveSlot.ACCESSORIES)
            }
            ActiveSlot.ACCESSORIES -> { /* stay */ }
        }
    }

    private fun setupClosetFilter() {
        val filterMap = mapOf(
            "tops"        to R.id.tvFilterTops,
            "bottoms"     to R.id.tvFilterBottoms,
            "shoes"       to R.id.tvFilterShoes,
            "accessories" to R.id.tvFilterAccessories,
            "all"         to R.id.tvFilterAll
        )

        filterMap.forEach { (cat, id) ->
            findViewById<TextView>(id).setOnClickListener {
                // Only allow manual filter changes when it's compatible with active slot
                val allowed = slotAllowedCategories[activeSlot] ?: listOf()
                if (cat != "all" && cat !in allowed) {
                    Toast.makeText(
                        this,
                        "Active slot only accepts: ${allowed.joinToString(", ")}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                filteredItems = if (cat == "all") {
                    allClosetItems.filter { it.category in (slotAllowedCategories[activeSlot] ?: listOf()) }
                } else {
                    allClosetItems.filter { it.category == cat }
                }
                closetAdapter.updateItems(filteredItems)
                updateFilterUI(cat)
            }
        }
    }

    private fun updateFilterUI(selected: String) {
        val filterMap = mapOf(
            "all"         to R.id.tvFilterAll,
            "tops"        to R.id.tvFilterTops,
            "bottoms"     to R.id.tvFilterBottoms,
            "shoes"       to R.id.tvFilterShoes,
            "accessories" to R.id.tvFilterAccessories
        )
        filterMap.forEach { (cat, id) ->
            findViewById<TextView>(id).apply {
                if (cat == selected) {
                    setBackgroundResource(R.drawable.bg_category_selected)
                    setTextColor(0xFF000000.toInt())
                } else {
                    setBackgroundResource(R.drawable.bg_category_unselected)
                    setTextColor(0xFFAAAAAA.toInt())
                }
            }
        }
    }

    private fun setupClosetGrid() {
        closetAdapter = ClosetAdapter(allClosetItems)
        val rv = findViewById<RecyclerView>(R.id.rvClosetForCanvas)
        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = closetAdapter

        rv.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: android.view.MotionEvent): Boolean {
                if (e.action == android.view.MotionEvent.ACTION_UP) {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        val position = rv.getChildAdapterPosition(child)
                        if (position >= 0 && position < filteredItems.size) {
                            fillActiveSlot(filteredItems[position])
                        }
                    }
                }
                return false
            }
        })
    }

    private fun setupSaveReset() {
        findViewById<MaterialButton>(R.id.btnSaveOutfit).setOnClickListener {
            val name = findViewById<EditText>(R.id.etOutfitName).text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter an outfit name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send outfit back to OutfitsActivity
            val resultIntent = Intent().apply {
                putExtra("outfit_name",     name)
                putExtra("outfit_category", selectedCategory)
                putExtra("outfit_item1",    slotTopRes)
                putExtra("outfit_item2",    slotBottomRes)
                putExtra("outfit_item3",    slotShoesRes)
                putExtra("outfit_item4",    slotAccessoriesRes ?: slotTopRes)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
            overridePendingTransition(0, 0)
        }

        findViewById<MaterialButton>(R.id.btnResetOutfit).setOnClickListener {
            slotTopRes = R.drawable.benchtee
            slotBottomRes = R.drawable.relaxedpants
            slotShoesRes = R.drawable.airforce
            slotAccessoriesRes = null

            findViewById<ImageView>(R.id.ivSlotTop).setImageResource(slotTopRes)
            findViewById<ImageView>(R.id.ivSlotBottom).setImageResource(slotBottomRes)
            findViewById<ImageView>(R.id.ivSlotShoes).setImageResource(slotShoesRes)

            val ivAcc = findViewById<ImageView>(R.id.ivSlotAccessories)
            ivAcc.visibility = View.GONE
            findViewById<View>(R.id.layoutAccessoriesEmpty).visibility = View.VISIBLE

            findViewById<EditText>(R.id.etOutfitName).text.clear()

            setActiveSlot(ActiveSlot.TOP)
            applySlotFilter(ActiveSlot.TOP)

            Toast.makeText(this, "Canvas reset", Toast.LENGTH_SHORT).show()
        }
    }
}
