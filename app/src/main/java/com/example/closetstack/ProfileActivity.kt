package com.example.closetstack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {

    // Single source of truth for the user's profile
    private lateinit var profile: UserProfile

    private val postImages = listOf(
        R.drawable.img_post1, R.drawable.img_post2, R.drawable.img_post3,
        R.drawable.img_post4, R.drawable.img_post5, R.drawable.img_post6,
        R.drawable.img_post1, R.drawable.img_post3, R.drawable.img_post5,
        R.drawable.img_post2, R.drawable.img_post4, R.drawable.img_post6
    )

    private val inspoItems = listOf(
        InspoItem(R.drawable.img_post1, "Summer fit"),
        InspoItem(R.drawable.img_post2, "Saturday boy"),
        InspoItem(R.drawable.img_post3, "Clean look"),
        InspoItem(R.drawable.img_post4, "Street style"),
        InspoItem(R.drawable.img_post5, "Layering fits"),
        InspoItem(R.drawable.img_post6, "Monochrome"),
        InspoItem(R.drawable.img_post1, "Vintage vibes"),
        InspoItem(R.drawable.img_post3, "Minimalist")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Load profile from persistence
        profile = ProfileRepository.loadProfile(this)

        bindProfile()
        setupViewPager()
        setupEditProfile()
        setupSettings()
        BottomNavManager.setup(this, NavScreen.PROFILE)
    }

    // Binds current profile data to all UI elements
    private fun bindProfile() {
        findViewById<TextView>(R.id.tvProfileUsername).text = profile.username
        findViewById<TextView>(R.id.tvDisplayName).text = profile.displayName
        findViewById<TextView>(R.id.tvBio).text = profile.bio
    }

    private fun setupViewPager() {
        val viewPager = findViewById<ViewPager2>(R.id.profileViewPager)
        val tabLayout = findViewById<TabLayout>(R.id.profileTabLayout)

        viewPager.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun getItemCount() = 2
            override fun getItemViewType(position: Int) = position

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val layoutId = if (viewType == 0) R.layout.fragment_profile_outfits
                else R.layout.fragment_profile_inspo
                val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                when (position) {
                    0 -> {
                        val rv = holder.itemView.findViewById<RecyclerView>(R.id.rvProfileOutfits)
                        rv.layoutManager = LinearLayoutManager(this@ProfileActivity)
                        rv.adapter = ProfilePostRowAdapter(postImages)
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
    }

    private fun setupEditProfile() {
        findViewById<MaterialButton>(R.id.btnEditProfile).setOnClickListener {
            showEditProfileSheet()
        }
    }

    private fun showEditProfileSheet() {
        val sheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_edit_profile, null)

        // Pre-fill all fields with current profile data
        view.findViewById<EditText>(R.id.etEditDisplayName).setText(profile.displayName)
        view.findViewById<EditText>(R.id.etEditUsername).setText(profile.username)
        view.findViewById<EditText>(R.id.etEditLocation).setText(profile.location)
        view.findViewById<EditText>(R.id.etAbout).setText(profile.bio)
        view.findViewById<EditText>(R.id.etInterests).setText(profile.interests)

        view.findViewById<ImageView>(R.id.ivEditBack).setOnClickListener {
            sheet.dismiss()
        }

        view.findViewById<MaterialButton>(R.id.btnSaveProfile).setOnClickListener {
            val newName     = view.findViewById<EditText>(R.id.etEditDisplayName).text.toString().trim()
            val newUsername = view.findViewById<EditText>(R.id.etEditUsername).text.toString().trim()
            val newLocation = view.findViewById<EditText>(R.id.etEditLocation).text.toString().trim()
            val newBio      = view.findViewById<EditText>(R.id.etAbout).text.toString().trim()
            val newInterests = view.findViewById<EditText>(R.id.etInterests).text.toString().trim()

            if (newName.isEmpty()) {
                Toast.makeText(this, "Display name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newUsername.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update profile object
            profile.displayName = newName
            profile.username    = newUsername
            profile.location    = newLocation
            profile.bio         = newBio
            profile.interests   = newInterests

            // Persist changes
            ProfileRepository.saveProfile(this@ProfileActivity, profile)

            // Reflect changes on screen immediately
            bindProfile()

            sheet.dismiss()
            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
        }

        sheet.setContentView(view)
        sheet.show()
    }

    private fun setupSettings() {
        findViewById<ImageView>(R.id.ivSettings).setOnClickListener {
            Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}