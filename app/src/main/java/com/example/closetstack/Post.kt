package com.example.closetstack

data class Post(
    val username: String,
    val description: String,
    val caption: String,
    val imageRes: Int = 0,
    val imageUri: String? = null,
    val avatarRes: Int,
    val timestamp: String,
    val feedType: String = "all",
    var isSaved: Boolean = false,
    var userRating: Float = 0f
)