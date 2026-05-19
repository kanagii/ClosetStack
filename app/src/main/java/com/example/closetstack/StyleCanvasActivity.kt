package com.example.closetstack

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class StyleCanvasActivity : AppCompatActivity() {

    private var slotTopRes: Int? = null
    private var slotBottomRes: Int? = null
    private var slotShoesRes: Int? = null
    private var slotAccessoriesRes: Int? = null

    private enum class ActiveSlot { TOP, BOTTOM, SHOES, ACCESSORIES }
    private var activeSlot = ActiveSlot.TOP

    private var selectedCategory = "casual"

    // Variable to keep track of the item currently being dragged
    private var currentlyDraggedItem: ClothingItem? = null

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
        setupDragAndDrop() // Initialize Drag and Drop Zones

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
                val iv = findViewById<ImageView>(R.id.ivSlotTop)
                iv.setImageResource(item.imageRes)
                iv.visibility = View.VISIBLE
                findViewById<View>(R.id.layoutTopEmpty).visibility = View.GONE
            }
            ActiveSlot.BOTTOM -> {
                slotBottomRes = item.imageRes
                val iv = findViewById<ImageView>(R.id.ivSlotBottom)
                iv.setImageResource(item.imageRes)
                iv.visibility = View.VISIBLE
                findViewById<View>(R.id.layoutBottomEmpty).visibility = View.GONE
            }
            ActiveSlot.SHOES -> {
                slotShoesRes = item.imageRes
                val iv = findViewById<ImageView>(R.id.ivSlotShoes)
                iv.setImageResource(item.imageRes)
                iv.visibility = View.VISIBLE
                findViewById<View>(R.id.layoutShoesEmpty).visibility = View.GONE
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

        // We use a GestureDetector to differentiate between a simple tap and a long press
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            // Allow users to still just tap to equip
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null) {
                    val position = rv.getChildAdapterPosition(child)
                    if (position >= 0 && position < filteredItems.size) {
                        fillActiveSlot(filteredItems[position])
                    }
                }
                return true
            }

            // Detect Long Press to trigger drag and drop
            override fun onLongPress(e: MotionEvent) {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null) {
                    val position = rv.getChildAdapterPosition(child)
                    if (position >= 0 && position < filteredItems.size) {

                        currentlyDraggedItem = filteredItems[position] // Save what we are holding

                        // ClipData is required for drag actions
                        val clipData = ClipData.newPlainText("clothing_item", position.toString())

                        // Creates a visually floating shadow of the view we are holding
                        val shadowBuilder = View.DragShadowBuilder(child)

                        // Start the actual drag
                        child.startDragAndDrop(clipData, shadowBuilder, null, 0)
                    }
                }
            }
        })

        rv.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                // Pass touch events to our new GestureDetector
                gestureDetector.onTouchEvent(e)
                return false
            }
        })
    }

    private fun setupDragAndDrop() {
        val dragListener = View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true // Signal that this view accepts drag events

                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Provide a cool visual cue when hovering over a slot (dim it slightly)
                    view.alpha = 0.7f
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    // Revert the visual cue if the user drags away
                    view.alpha = 1.0f
                    true
                }

                DragEvent.ACTION_DROP -> {
                    // The user released their finger!
                    view.alpha = 1.0f
                    val droppedItem = currentlyDraggedItem

                    if (droppedItem != null) {
                        // Find out which slot we just dropped the item onto
                        val targetSlot = when (view.id) {
                            R.id.slotTop -> ActiveSlot.TOP
                            R.id.slotBottom -> ActiveSlot.BOTTOM
                            R.id.slotShoes -> ActiveSlot.SHOES
                            R.id.slotAccessories -> ActiveSlot.ACCESSORIES
                            else -> null
                        }

                        if (targetSlot != null) {
                            val allowed = slotAllowedCategories[targetSlot] ?: listOf()

                            if (droppedItem.category in allowed) {
                                // If allowed, momentarily set the active slot and fill it!
                                setActiveSlot(targetSlot)
                                fillActiveSlot(droppedItem)
                            } else {
                                // Provide feedback if they drop a shoe onto the top slot
                                val allowedStr = allowed.joinToString(" or ")
                                Toast.makeText(this, "This slot only accepts $allowedStr items", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    currentlyDraggedItem = null // Reset our tracker
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    view.alpha = 1.0f // Ensure alpha is restored in case something went wrong
                    currentlyDraggedItem = null
                    true
                }
                else -> false
            }
        }

        // Attach this universal drag listener to all 4 of your slot CardViews
        findViewById<View>(R.id.slotTop).setOnDragListener(dragListener)
        findViewById<View>(R.id.slotBottom).setOnDragListener(dragListener)
        findViewById<View>(R.id.slotShoes).setOnDragListener(dragListener)
        findViewById<View>(R.id.slotAccessories).setOnDragListener(dragListener)
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
                putExtra("outfit_item1",    slotTopRes ?: 0)
                putExtra("outfit_item2",    slotBottomRes ?: 0)
                putExtra("outfit_item3",    slotShoesRes ?: 0)
                putExtra("outfit_item4",    slotAccessoriesRes ?: 0)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
            overridePendingTransition(0, 0)
        }

        findViewById<MaterialButton>(R.id.btnResetOutfit).setOnClickListener {
            slotTopRes = null
            slotBottomRes = null
            slotShoesRes = null
            slotAccessoriesRes = null

            findViewById<ImageView>(R.id.ivSlotTop).visibility = View.GONE
            findViewById<View>(R.id.layoutTopEmpty).visibility = View.VISIBLE

            findViewById<ImageView>(R.id.ivSlotBottom).visibility = View.GONE
            findViewById<View>(R.id.layoutBottomEmpty).visibility = View.VISIBLE

            findViewById<ImageView>(R.id.ivSlotShoes).visibility = View.GONE
            findViewById<View>(R.id.layoutShoesEmpty).visibility = View.VISIBLE

            findViewById<ImageView>(R.id.ivSlotAccessories).visibility = View.GONE
            findViewById<View>(R.id.layoutAccessoriesEmpty).visibility = View.VISIBLE

            findViewById<EditText>(R.id.etOutfitName).text.clear()

            setActiveSlot(ActiveSlot.TOP)
            applySlotFilter(ActiveSlot.TOP)

            Toast.makeText(this, "Canvas reset", Toast.LENGTH_SHORT).show()
        }
    }
}