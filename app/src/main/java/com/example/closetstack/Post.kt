package com.example.closetstack

data class Post(
    val username: String,
    val description: String,
    val caption: String,
    val imageRes: Int,
    val avatarRes: Int,
    val timestamp: String,
    val feedType: String = "all",  // "all", "following", "follower"
    var isSaved: Boolean = false,
    var userRating: Float = 0f
)