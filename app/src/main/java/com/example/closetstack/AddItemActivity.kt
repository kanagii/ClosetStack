package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class AddItemActivity : AppCompatActivity() {

    private var selectedSize = ""
    private var selectedColor = ""

    private val sizeIds = listOf(
        R.id.sizeXS to "XS", R.id.sizeS to "S", R.id.sizeM to "M",
        R.id.sizeL to "L", R.id.sizeXL to "XL", R.id.sizeXXL to "XXL",
        R.id.sizeFreeSize to "Free Size"
    )

    private val colorIds = listOf(
        R.id.colorRed, R.id.colorOrange, R.id.colorYellow, R.id.colorGreen,
        R.id.colorBlue, R.id.colorPurple, R.id.colorPink,
        R.id.colorBrown, R.id.colorBeige, R.id.colorGray,
        R.id.colorBlack, R.id.colorWhite
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        setupBack()
        setupSpinners()
        setupSizeSelector()
        setupColorSelector()
        setupPhotoButtons()
        setupSaveButton()
        setupBottomNav()
    }

    private fun setupBack() {
        findViewById<View>(R.id.ivBack).setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun setupSpinners() {
        val categories = listOf("tops", "bottoms", "outer", "accessories", "shoes")
        val subcategories = listOf("t-shirt", "polo", "blouse", "hoodie", "jacket", "pants", "skirt", "shorts")

        val catAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        findViewById<AppCompatSpinner>(R.id.spinnerCategory).adapter = catAdapter

        val subAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subcategories)
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        findViewById<AppCompatSpinner>(R.id.spinnerSubcategory).adapter = subAdapter
    }

    private fun setupSizeSelector() {
        sizeIds.forEach { (id, size) ->
            findViewById<TextView>(id).setOnClickListener {
                // Reset all
                sizeIds.forEach { (otherId, _) ->
                    findViewById<TextView>(otherId).apply {
                        setBackgroundResource(R.drawable.bg_category_unselected)
                        setTextColor(0xFFAAAAAA.toInt())
                    }
                }
                // Select tapped
                selectedSize = size
                (it as TextView).apply {
                    setBackgroundResource(R.drawable.bg_size_selected)
                    setTextColor(0xFFFFFFFF.toInt())
                }
            }
        }
    }

    private fun setupColorSelector() {
        colorIds.forEach { id ->
            findViewById<View>(id).setOnClickListener { clicked ->
                // Reset all — remove stroke by re-setting background
                colorIds.forEach { otherId ->
                    val v = findViewById<View>(otherId)
                    if (otherId == R.id.colorWhite) {
                        v.setBackgroundResource(R.drawable.bg_color_swatch_outline)
                    } else {
                        v.setBackgroundResource(R.drawable.bg_color_swatch)
                    }
                    v.scaleX = 1f
                    v.scaleY = 1f
                }
                // Highlight selected
                selectedColor = clicked.tag?.toString() ?: id.toString()
                clicked.animate().scaleX(1.25f).scaleY(1.25f).setDuration(150).start()
            }
        }
    }

    private fun setupPhotoButtons() {
        findViewById<View>(R.id.cardPhotoUpload).setOnClickListener {
            Toast.makeText(this, "Photo upload coming soon!", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btnCamera).setOnClickListener {
            Toast.makeText(this, "Camera coming soon!", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btnGallery).setOnClickListener {
            Toast.makeText(this, "Gallery coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSaveButton() {
        findViewById<MaterialButton>(R.id.btnSaveItem).setOnClickListener {
            Toast.makeText(this, "Item saved! (coming soon)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNav() {
        findViewById<BottomNavigationView>(R.id.bottomNav).apply {
            selectedItemId = R.id.nav_closet
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        startActivity(Intent(this@AddItemActivity, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_closet -> {
                        startActivity(Intent(this@AddItemActivity, ClosetActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_outfits -> {
                        Toast.makeText(this@AddItemActivity, "Outfits coming soon!", Toast.LENGTH_SHORT).show()
                        false
                    }
                    R.id.nav_profile -> {
                        Toast.makeText(this@AddItemActivity, "Profile coming soon!", Toast.LENGTH_SHORT).show()
                        false
                    }
                    else -> false
                }
            }
        }
    }
}