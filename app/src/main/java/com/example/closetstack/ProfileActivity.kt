package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {

    private val outfits = listOf(
        Outfit("Hype Boy", "4 items · casual", "casual", R.drawable.img_post1, R.drawable.img_post2, R.drawable.img_post3, R.drawable.img_post4),
        Outfit("Oversized Feels", "4 items · streetwear", "streetwear", R.drawable.img_post5, R.drawable.img_post6, R.drawable.img_post1, R.drawable.img_post2),
        Outfit("Saturday", "3 items · casual", "casual", R.drawable.img_post3, R.drawable.img_post4, R.drawable.img_post5, R.drawable.img_post6),
        Outfit("Strt apoy", "4 items · streetwear", "streetwear", R.drawable.img_post2, R.drawable.img_post3, R.drawable.img_post1, R.drawable.img_post4)
    )

    private val inspoItems = listOf(
        InspoItem(R.drawable.img_post1, "Summer fit"),
        InspoItem(R.drawable.img_post2, "Saturday boy"),
        InspoItem(R.drawable.img_post3, "Clean look"),
        InspoItem(R.drawable.img_post4, "Street style"),
        InspoItem(R.drawable.img_post5, "Layering fits"),
        InspoItem(R.drawable.img_post6, "Monochrome"),
        InspoItem(R.drawable.graypost, "Vintage vibes"),
        InspoItem(R.drawable.whitepost, "Minimalist")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupViewPager()
        setupEditProfile()
        setupSettings()
        setupBottomNav()
    }

    private fun setupViewPager() {
        val viewPager = findViewById<ViewPager2>(R.id.profileViewPager)
        val tabLayout = findViewById<TabLayout>(R.id.profileTabLayout)

        viewPager.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun getItemCount() = 2

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return when (viewType) {
                    0 -> {
                        val view = LayoutInflater.from(parent.context)
                            .inflate(R.layout.fragment_profile_outfits, parent, false)
                        object : RecyclerView.ViewHolder(view) {}
                    }
                    else -> {
                        val view = LayoutInflater.from(parent.context)
                            .inflate(R.layout.fragment_profile_inspo, parent, false)
                        object : RecyclerView.ViewHolder(view) {}
                    }
                }
            }

            override fun getItemViewType(position: Int) = position

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                when (position) {
                    0 -> {
                        val rv = holder.itemView.findViewById<RecyclerView>(R.id.rvProfileOutfits)
                        rv.layoutManager = GridLayoutManager(this@ProfileActivity, 2)
                        rv.adapter = OutfitAdapter(outfits) {
                            startActivity(Intent(this@ProfileActivity, StyleCanvasActivity::class.java))
                            overridePendingTransition(0, 0)
                        }
                    }
                    1 -> {
                        val rv = holder.itemView.findViewById<RecyclerView>(R.id.rvProfileInspo)
                        rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        rv.adapter = InspoAdapter(inspoItems)
                    }
                }
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "OUTFITS" else "INSPO"
        }.attach()

        // Long press on OUTFITS tab → Edit Header sheet
        tabLayout.getTabAt(0)?.view?.setOnLongClickListener {
            showEditHeaderSheet()
            true
        }
    }

    private fun setupEditProfile() {
        findViewById<MaterialButton>(R.id.btnEditProfile).setOnClickListener {
            showEditProfileSheet()
        }
    }

    private fun showEditProfileSheet() {
        val sheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_edit_profile, null)

        view.findViewById<View>(R.id.ivEditBack).setOnClickListener { sheet.dismiss() }
        view.findViewById<MaterialButton>(R.id.btnSaveProfile).setOnClickListener {
            Toast.makeText(this, "Profile saved! (coming soon)", Toast.LENGTH_SHORT).show()
            sheet.dismiss()
        }

        sheet.setContentView(view)
        sheet.show()
    }

    private fun showEditHeaderSheet() {
        val sheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_edit_header, null)

        // Symbol selection
        val symIds = listOf(
            R.id.sym1, R.id.sym2, R.id.sym3, R.id.sym4, R.id.sym5, R.id.sym6, R.id.sym7,
            R.id.sym8, R.id.sym9, R.id.sym10, R.id.sym11, R.id.sym12, R.id.sym13, R.id.sym14
        )
        var selectedSymId = R.id.sym1

        symIds.forEach { id ->
            view.findViewById<TextView>(id).setOnClickListener {
                view.findViewById<TextView>(selectedSymId)
                    .setBackgroundResource(R.drawable.bg_symbol_unselected)
                selectedSymId = id
                view.findViewById<TextView>(id)
                    .setBackgroundResource(R.drawable.bg_symbol_selected)
            }
        }

        view.findViewById<MaterialButton>(R.id.btnSaveHeader).setOnClickListener {
            Toast.makeText(this, "Header saved! (coming soon)", Toast.LENGTH_SHORT).show()
            sheet.dismiss()
        }

        sheet.setContentView(view)
        sheet.show()
    }

    private fun setupSettings() {
        findViewById<ImageView>(R.id.ivSettings).setOnClickListener {
            Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNav() {
        findViewById<BottomNavigationView>(R.id.bottomNav).apply {
            selectedItemId = R.id.nav_profile
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        startActivity(Intent(this@ProfileActivity, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_closet -> {
                        startActivity(Intent(this@ProfileActivity, ClosetActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_outfits -> {
                        startActivity(Intent(this@ProfileActivity, OutfitsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                        true
                    }
                    R.id.nav_profile -> true
                    else -> false
                }
            }
        }
    }
}