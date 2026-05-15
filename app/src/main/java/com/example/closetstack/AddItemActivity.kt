package com.example.closetstack

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class AddItemActivity : AppCompatActivity() {

    private var selectedSize = ""
    private var selectedColor = ""
    private var currentRing: View? = null

    private val colorPairs = listOf(
        R.id.colorRed to R.id.ringRed,
        R.id.colorOrange to R.id.ringOrange,
        R.id.colorYellow to R.id.ringYellow,
        R.id.colorGreen to R.id.ringGreen,
        R.id.colorBlue to R.id.ringBlue,
        R.id.colorPurple to R.id.ringPurple,
        R.id.colorPink to R.id.ringPink,
        R.id.colorBrown to R.id.ringBrown,
        R.id.colorBeige to R.id.ringBeige,
        R.id.colorGray to R.id.ringGray,
        R.id.colorBlack to R.id.ringBlack,
        R.id.colorWhite to R.id.ringWhite
    )

    private val sizeIds = listOf(
        R.id.sizeXS to "XS", R.id.sizeS to "S", R.id.sizeM to "M",
        R.id.sizeL to "L", R.id.sizeXL to "XL", R.id.sizeXXL to "XXL",
        R.id.sizeFreeSize to "Free Size"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        setupBack()
        setupSpinners()
        setupAutocomplete()
        setupSizeSelector()
        setupColorSelector()
        setupPhotoButtons()
        setupSaveButton()
        setupBottomNav()
    }

    private fun dp(value: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics).toInt()

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

    private fun setupAutocomplete() {
        val brands = listOf(
            "BENCH", "Uniqlo", "H&M", "Zara", "Nike", "Adidas", "Puma", "Reebok",
            "Supreme", "Bershka", "Pull&Bear", "Mango", "Forever 21", "Levi's",
            "Tommy Hilfiger", "Lacoste", "Gucci", "Louis Vuitton", "Balenciaga", "Off-White"
        )

        val tagList = listOf(
            "casual", "streetwear", "formal", "vintage", "minimalist", "sporty",
            "aesthetic", "grunge", "preppy", "boho", "oversized", "fitted",
            "monochrome", "colorful", "layered", "summer", "winter", "fall",
            "spring", "thrifted"
        )

        // Brand autocomplete
        val brandAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            brands
        )
        val etBrand = findViewById<AutoCompleteTextView>(R.id.etBrand)
        etBrand.setAdapter(brandAdapter)
        etBrand.setTextColor(0xFFFFFFFF.toInt())
        etBrand.setHintTextColor(0xFF555555.toInt())

        // Tags autocomplete
        val etTags = findViewById<AutoCompleteTextView>(R.id.etTags)
        etTags.setTextColor(0xFFFFFFFF.toInt())
        etTags.setHintTextColor(0xFF555555.toInt())

        val tagAdapter = TagAutoCompleteAdapter(this, tagList, etTags)
        etTags.setAdapter(tagAdapter)
        etTags.threshold = 1
    }

    private fun setupSizeSelector() {
        sizeIds.forEach { (id, size) ->
            findViewById<TextView>(id).setOnClickListener {
                sizeIds.forEach { (otherId, _) ->
                    findViewById<TextView>(otherId).apply {
                        setBackgroundResource(R.drawable.bg_category_unselected)
                        setTextColor(0xFFAAAAAA.toInt())
                    }
                }
                selectedSize = size
                (it as TextView).apply {
                    setBackgroundResource(R.drawable.bg_size_selected)
                    setTextColor(0xFFFFFFFF.toInt())
                }
            }
        }
    }

    private fun setupColorSelector() {
        colorPairs.forEach { (swatchId, ringId) ->
            val ring = findViewById<View>(ringId)
            // Click the FrameLayout parent of the swatch
            (findViewById<View>(swatchId).parent as? View)?.setOnClickListener {
                selectColor(ring)
            }
        }

        findViewById<View>(R.id.btnAddColor).setOnClickListener {
            showColorWheelDialog()
        }
    }

    private fun selectColor(selectedRing: View) {
        currentRing?.visibility = View.GONE
        selectedRing.visibility = View.VISIBLE
        currentRing = selectedRing
    }

    private fun showColorWheelDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_color_picker)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val colorWheel = dialog.findViewById<ColorWheelView>(R.id.colorWheelView)
        val sbBrightness = dialog.findViewById<SeekBar>(R.id.sbBrightness)
        val vPreview = dialog.findViewById<View>(R.id.vColorPreview)
        val btnConfirm = dialog.findViewById<MaterialButton>(R.id.btnConfirmColor)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancelColor)

        var pickedColor = Color.RED
        vPreview.setBackgroundColor(pickedColor)

        colorWheel.setColorChangeListener { color ->
            pickedColor = color
            vPreview.setBackgroundColor(color)
        }

        sbBrightness.progress = 100
        sbBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                val hsv = FloatArray(3)
                Color.colorToHSV(pickedColor, hsv)
                hsv[2] = progress / 100f
                pickedColor = Color.HSVToColor(hsv)
                vPreview.setBackgroundColor(pickedColor)
            }
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        })

        btnConfirm.setOnClickListener {
            addCustomColorSwatch(pickedColor)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun addCustomColorSwatch(color: Int) {
        val row2 = findViewById<LinearLayout>(R.id.colorRow2)
        val addBtn = findViewById<View>(R.id.btnAddColor)

        val swatchSize = dp(28)
        val frameSize = dp(36)
        val marginEnd = dp(8)

        val frame = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(frameSize, frameSize).also {
                it.marginEnd = marginEnd
            }
        }

        val swatch = View(this).apply {
            layoutParams = FrameLayout.LayoutParams(swatchSize, swatchSize).also {
                it.gravity = Gravity.CENTER
            }
            setBackgroundResource(R.drawable.bg_color_swatch)
            backgroundTintList = ColorStateList.valueOf(color)
        }

        val ring = View(this).apply {
            layoutParams = FrameLayout.LayoutParams(frameSize, frameSize)
            setBackgroundResource(R.drawable.bg_color_swatch_ring)
            visibility = View.GONE
        }

        frame.addView(swatch)
        frame.addView(ring)

        val index = row2.indexOfChild(addBtn)
        row2.addView(frame, index)

        frame.setOnClickListener { selectColor(ring) }
        selectColor(ring)
        selectedColor = String.format("#%06X", 0xFFFFFF and color)
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