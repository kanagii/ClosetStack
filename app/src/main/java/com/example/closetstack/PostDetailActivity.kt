package com.example.closetstack

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val imageRes = intent.getIntExtra("imageRes", 0)

        findViewById<ImageView>(R.id.ivFullImage).setImageResource(imageRes)
        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }
    }
}