package com.example.closetstack

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OutfitDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfit_details)

        setupBack()
        displayOutfit()
    }

    private fun setupBack() {
        findViewById<View>(R.id.ivBack).setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun displayOutfit() {
        val outfit = intent.getSerializableExtra("EXTRA_OUTFIT") as? Outfit ?: return

        findViewById<TextView>(R.id.tvDetailName).text = outfit.name
        findViewById<TextView>(R.id.tvDetailCategory).text = outfit.category.uppercase()
        findViewById<TextView>(R.id.tvDetailDesc).text = outfit.description

        findViewById<ImageView>(R.id.ivDetailItem1).setImageResource(outfit.item1Res)
        findViewById<ImageView>(R.id.ivDetailItem2).setImageResource(outfit.item2Res)
        findViewById<ImageView>(R.id.ivDetailItem3).setImageResource(outfit.item3Res)
        findViewById<ImageView>(R.id.ivDetailItem4).setImageResource(outfit.item4Res)
    }
}