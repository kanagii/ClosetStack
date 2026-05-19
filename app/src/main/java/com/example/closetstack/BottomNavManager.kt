package com.example.closetstack

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

enum class NavScreen { HOME, CLOSET, OUTFITS, PROFILE }

object BottomNavManager {

    fun setup(
        activity: Activity,
        currentScreen: NavScreen
    ) {
        val profile = ProfileRepository.loadProfile(activity)
        val avatarRes = profile.avatarRes

        val navHome    = activity.findViewById<LinearLayout>(R.id.navHome)
        val navCloset  = activity.findViewById<LinearLayout>(R.id.navCloset)
        val navOutfits = activity.findViewById<LinearLayout>(R.id.navOutfits)
        val navProfile = activity.findViewById<LinearLayout>(R.id.navProfile)

        val ivHome    = activity.findViewById<ImageView>(R.id.ivNavHome)
        val ivCloset  = activity.findViewById<ImageView>(R.id.ivNavCloset)
        val ivOutfits = activity.findViewById<ImageView>(R.id.ivNavOutfits)
        val ivProfile = activity.findViewById<ImageView>(R.id.ivNavProfile)

        // Set circular avatar on profile icon
        setCircularAvatar(activity, ivProfile, avatarRes)

        // Apply initial selected state
        applySelection(currentScreen, navHome, navCloset, navOutfits, navProfile,
            ivHome, ivCloset, ivOutfits, ivProfile)

        // Click listeners
        navHome.setOnClickListener {
            if (currentScreen != NavScreen.HOME) {
                activity.startActivity(Intent(activity, HomeActivity::class.java))
                activity.overridePendingTransition(0, 0)
                activity.finish()
            }
        }

        navCloset.setOnClickListener {
            if (currentScreen != NavScreen.CLOSET) {
                activity.startActivity(Intent(activity, ClosetActivity::class.java))
                activity.overridePendingTransition(0, 0)
                activity.finish()
            }
        }

        navOutfits.setOnClickListener {
            if (currentScreen != NavScreen.OUTFITS) {
                activity.startActivity(Intent(activity, OutfitsActivity::class.java))
                activity.overridePendingTransition(0, 0)
                activity.finish()
            }
        }

        navProfile.setOnClickListener {
            if (currentScreen != NavScreen.PROFILE) {
                activity.startActivity(Intent(activity, ProfileActivity::class.java))
                activity.overridePendingTransition(0, 0)
                activity.finish()
            }
        }
    }

    private fun applySelection(
        current: NavScreen,
        navHome: LinearLayout, navCloset: LinearLayout,
        navOutfits: LinearLayout, navProfile: LinearLayout,
        ivHome: ImageView, ivCloset: ImageView,
        ivOutfits: ImageView, ivProfile: ImageView
    ) {
        // Reset all to unselected
        setUnselected(navHome, ivHome, isAvatar = false)
        setUnselected(navCloset, ivCloset, isAvatar = false)
        setUnselected(navOutfits, ivOutfits, isAvatar = false, isCenterPill = true)
        setUnselected(navProfile, ivProfile, isAvatar = true)

        // Apply selected
        when (current) {
            NavScreen.HOME    -> setSelected(navHome, ivHome, isAvatar = false)
            NavScreen.CLOSET  -> setSelected(navCloset, ivCloset, isAvatar = false)
            NavScreen.OUTFITS -> setSelected(navOutfits, ivOutfits, isAvatar = false, isCenterPill = true)
            NavScreen.PROFILE -> setSelected(navProfile, ivProfile, isAvatar = true)
        }
    }

    private fun setSelected(
        container: LinearLayout,
        icon: ImageView,
        isAvatar: Boolean,
        isCenterPill: Boolean = false
    ) {
        if (isAvatar) {
            icon.setBackgroundResource(R.drawable.bg_nav_avatar_selected)
        } else {
            container.setBackgroundResource(R.drawable.bg_nav_selected)
            icon.imageTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#1A1A2E")
            )
        }
    }

    private fun setUnselected(
        container: LinearLayout,
        icon: ImageView,
        isAvatar: Boolean,
        isCenterPill: Boolean = false
    ) {
        if (isAvatar) {
            icon.setBackgroundResource(R.drawable.bg_nav_avatar_unselected)
        } else {
            container.setBackgroundResource(android.R.color.transparent)
            icon.imageTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#555566")
            )
        }
    }

    private fun setCircularAvatar(activity: Activity, imageView: ImageView, avatarRes: Int) {
        try {
            val original = BitmapFactory.decodeResource(activity.resources, avatarRes)
            if (original == null) {
                imageView.setImageResource(R.drawable.ic_person)
                return
            }
            val size = 80
            val scaled = Bitmap.createScaledBitmap(original, size, size, true)
            val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.shader = BitmapShader(scaled, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
            imageView.setImageBitmap(output)
            imageView.imageTintList = null // clear tint so avatar shows real colors
        } catch (e: Exception) {
            imageView.setImageResource(R.drawable.ic_person)
        }
    }
}