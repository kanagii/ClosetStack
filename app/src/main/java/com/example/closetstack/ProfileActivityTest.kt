package com.example.closetstack

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

class ProfileActivityTest : AppCompatActivity() {

    private val avatarRes = R.drawable.usertop1

    private fun getProfilePostImages(): List<ProfilePostImage> {
        val userImages = PostRepository.getUserPosts().map {
            if (it.imageUri != null) ProfilePostImage.Uri(it.imageUri)
            else ProfilePostImage.Res(it.imageRes)
        }
        val seeded = listOf(
            ProfilePostImage.Res(R.drawable.img_post1),
            ProfilePostImage.Res(R.drawable.img_post2),
            ProfilePostImage.Res(R.drawable.img_post3),
            ProfilePostImage.Res(R.drawable.img_post4),
            ProfilePostImage.Res(R.drawable.img_post5),
            ProfilePostImage.Res(R.drawable.img_post6),
            ProfilePostImage.Res(R.drawable.img_post1),
            ProfilePostImage.Res(R.drawable.img_post3),
            ProfilePostImage.Res(R.drawable.img_post5),
            ProfilePostImage.Res(R.drawable.img_post2),
            ProfilePostImage.Res(R.drawable.img_post4),
            ProfilePostImage.Res(R.drawable.img_post6)
        )
        return userImages + seeded
    }

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
        setContentView(R.layout.activity_profile_test)

        setupViewPager()
        setupEditProfile()
        setupSettings()
        BottomNavManager.setup(this, NavScreen.PROFILE, avatarRes)
    }

    override fun onResume() {
        super.onResume()
        setupViewPager()  // re-bind with fresh data
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
                        rv.layoutManager = LinearLayoutManager(this@ProfileActivityTest)
                        rv.adapter = ProfilePostRowAdapter(getProfilePostImages())
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
    }

    private fun setupSettings() {
        findViewById<ImageView>(R.id.ivSettings).setOnClickListener {
            Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}