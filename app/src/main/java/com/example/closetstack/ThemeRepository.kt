package com.example.closetstack

import android.content.Context
import android.content.SharedPreferences

class ThemeRepository(context: Context) {
    
    // Encapsulate the SharedPreferences instance
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Save the theme ID (e.g., R.style.Theme_ClosetStack_BubbleGum)
    fun saveTheme(themeResId: Int) {
        prefs.edit().putInt(KEY_THEME, themeResId).apply() // .apply() is asynchronous, preventing UI thread blocking
    }

    // Retrieve the theme ID, defaulting to Paper Zine if none is saved
    fun getTheme(): Int {
        return prefs.getInt(KEY_THEME, R.style.Theme_ClosetStack_DefaultDark)
    }

    companion object {
        private const val PREFS_NAME = "techtech_theme_prefs"
        private const val KEY_THEME = "selected_theme"
    }
}
