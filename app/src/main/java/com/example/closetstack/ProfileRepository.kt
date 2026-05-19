package com.example.closetstack

import android.content.Context
import android.content.SharedPreferences

object ProfileRepository {
    private const val PREFS_NAME = "closet_stack_prefs"
    private const val KEY_DISPLAY_NAME = "display_name"
    private const val KEY_USERNAME = "username"
    private const val KEY_LOCATION = "location"
    private const val KEY_BIO = "bio"
    private const val KEY_INTERESTS = "interests"
    private const val KEY_AVATAR_RES = "avatar_res"

    fun saveProfile(context: Context, profile: UserProfile) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val resName = try {
            context.resources.getResourceEntryName(profile.avatarRes)
        } catch (e: Exception) {
            "usertop1"
        }
        prefs.edit().apply {
            putString(KEY_DISPLAY_NAME, profile.displayName)
            putString(KEY_USERNAME, profile.username)
            putString(KEY_LOCATION, profile.location)
            putString(KEY_BIO, profile.bio)
            putString(KEY_INTERESTS, profile.interests)
            putString(KEY_AVATAR_RES, resName)
            apply()
        }
    }

    fun loadProfile(context: Context): UserProfile {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // Robust resource loading: check if saved as String (name) or Int (legacy ID)
        val savedAvatar = prefs.all[KEY_AVATAR_RES]
        val avatarRes = when (savedAvatar) {
            is String -> {
                val resId = context.resources.getIdentifier(savedAvatar, "drawable", context.packageName)
                if (resId != 0) resId else R.drawable.usertop1
            }
            is Int -> savedAvatar
            else -> R.drawable.usertop1
        }

        return UserProfile(
            displayName = prefs.getString(KEY_DISPLAY_NAME, "Vanessa Kung") ?: "Vanessa Kung",
            username = prefs.getString(KEY_USERNAME, "vanessa_kung") ?: "vanessa_kung",
            location = prefs.getString(KEY_LOCATION, "Philippines") ?: "Philippines",
            bio = prefs.getString(KEY_BIO, "Fashion and Styling Enthusiast") ?: "Fashion and Styling Enthusiast",
            interests = prefs.getString(KEY_INTERESTS, "#Core  #FreeNovaJeans  #ForTheStreets\n#StreetWear  #hiphop") ?: "#Core  #FreeNovaJeans  #ForTheStreets\n#StreetWear  #hiphop",
            avatarRes = avatarRes
        )
    }
}
