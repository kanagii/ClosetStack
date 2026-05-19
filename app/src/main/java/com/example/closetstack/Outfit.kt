package com.example.closetstack

import java.io.Serializable

data class Outfit(
    val name: String,
    val description: String,
    val category: String,
    val item1Res: Int,
    val item2Res: Int,
    val item3Res: Int,
    val item4Res: Int
) : Serializable