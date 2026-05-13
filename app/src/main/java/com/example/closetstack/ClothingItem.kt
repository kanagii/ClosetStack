package com.example.closetstack

data class ClothingItem(
    val name: String,
    val imageRes: Int,
    val category: String  // "all", "tops", "bottoms", "outer", "accessories", "shoes"
)