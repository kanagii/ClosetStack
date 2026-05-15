package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class StyleCanvasActivity : AppCompatActivity() {

    // Currently selected slot to fill
    private var activeSlot: ImageView? = null
    private var activeSlotView: View? = null

    private val slotIds = listOf(
        R.id.ivSlotTop, R.id.ivSlotBottom, R.id.ivSlotShoes
    )

    private val closetItems = listOf(
        ClothingItem("BENCH Unisex Tee", R.drawable.img_post1, "tops"),
        ClothingItem("Bella Flowy Croptop", R.drawable.img_post2, "tops"),
        ClothingItem("Taelor White Shirt", R.drawable.img_post3, "tops"),
        ClothingItem("Bello Flary Chino", R.drawable.img_post4, "bottoms"),
        ClothingItem("UNIQLO Relaxed Pants", R.drawable.img_post5, "bottoms"),
        ClothingItem("SHEIN Plaid Skirt", R.drawable.img_post6, "bottoms"),
        ClothingItem("Nike Air Force 1", R.drawable.img_post1, "shoes"),
        ClothingItem("Adidas Samba", R.drawable.img_post2, "shoes"),
        ClothingItem("Converse Chuck 70", R.drawable.img_post3, "shoes")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style_canvas)

        setupBack()
        setupSlots()
        setupSuggestButtons()
        setupClosetGrid()
        setupSaveReset()
    }

    private fun setupBack() {
        findViewById<View>(R.id.ivBack).setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun setupSlots() {
        // Tap a slot to make it "active" so next closet pick fills it
        val slotTop = findViewById<ImageView>(R.id.ivSlotTop)
        val slotBottom = findViewById<ImageView>(R.id.ivSlotBottom)
        val slotShoes = findViewById<ImageView>(R.id.ivSlotShoes)
        val slotAccessories = findViewById<View>(R.id.slotAccessories)

        fun setActive(iv: ImageView, card: View) {
            activeSlot = iv
            activeSlotView = card
            // Visual feedback: slight alpha on inactive slots
            listOf(
                slotTop to findViewById<View>(R.id.slotTop),
                slotBottom to findViewById(R.id.slotBottom),
                slotShoes to findViewById(R.id.slotShoes)
            ).forEach { (imgView, cardView) ->
                cardView.alpha = if (imgView == iv) 1f else 0.6f
            }
        }

        slotTop.setOnClickListener { setActive(slotTop, findViewById(R.id.slotTop)) }
        slotBottom.setOnClickListener { setActive(slotBottom, findViewById(R.id.slotBottom)) }
        slotShoes.setOnClickListener { setActive(slotShoes, findViewById(R.id.slotShoes)) }
        slotAccessories.setOnClickListener {
            Toast.makeText(this, "Tap an item from closet to add as accessory", Toast.LENGTH_SHORT).show()
        }

        // Default active slot = top
        setActive(slotTop, findViewById(R.id.slotTop))
    }

    private fun setupSuggestButtons() {
        listOf(R.id.ivAddSuggest1, R.id.ivAddSuggest2, R.id.ivAddSuggest3).zip(
            listOf(R.id.ivSuggest1, R.id.ivSuggest2, R.id.ivSuggest3)
        ).forEach { (btnId, suggestId) ->
            findViewById<View>(btnId).setOnClickListener {
                val suggestedImage = (findViewById<ImageView>(suggestId)).drawable
                activeSlot?.setImageDrawable(suggestedImage)
                activeSlotView?.alpha = 1f
                Toast.makeText(this, "Suggestion added to canvas!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClosetGrid() {
        val adapter = ClosetAdapter(closetItems)
        val rv = findViewById<RecyclerView>(R.id.rvClosetForCanvas)
        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = adapter

        // Override item click to fill active slot
        rv.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: android.view.MotionEvent): Boolean {
                if (e.action == android.view.MotionEvent.ACTION_UP) {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        val position = rv.getChildAdapterPosition(child)
                        if (position != RecyclerView.NO_ID.toInt()) {
                            val item = closetItems[position]
                            activeSlot?.setImageResource(item.imageRes)
                            activeSlotView?.alpha = 1f
                        }
                    }
                }
                return false
            }
        })
    }

    private fun setupSaveReset() {
        findViewById<MaterialButton>(R.id.btnSaveOutfit).setOnClickListener {
            Toast.makeText(this, "Outfit saved! (coming soon)", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.btnResetOutfit).setOnClickListener {
            // Reset all slots to defaults
            findViewById<ImageView>(R.id.ivSlotTop).setImageResource(R.drawable.img_post1)
            findViewById<ImageView>(R.id.ivSlotBottom).setImageResource(R.drawable.img_post2)
            findViewById<ImageView>(R.id.ivSlotShoes).setImageResource(R.drawable.img_post3)
            Toast.makeText(this, "Canvas reset", Toast.LENGTH_SHORT).show()
        }
    }
}