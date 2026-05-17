package com.example.closetstack

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

object NavAvatarHelper {

    fun setCircularAvatar(
        bottomNav: BottomNavigationView,
        resources: Resources,
        avatarRes: Int
    ) {
        try {
            val original = BitmapFactory.decodeResource(resources, avatarRes)
            val size = 80 // px — a good size for nav icons
            val scaled = Bitmap.createScaledBitmap(original, size, size, true)

            val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.shader = BitmapShader(scaled, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

            bottomNav.menu.findItem(R.id.nav_profile)?.icon =
                BitmapDrawable(resources, output)
        } catch (e: Exception) {
            // silently fall back to default icon
        }
    }
}