package com.example.closetstack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    
    protected lateinit var themeRepository: ThemeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Initialize the Model
        themeRepository = ThemeRepository(this)
        
        // 2. Fetch the saved theme ($O(1)$ retrieval)
        val savedTheme = themeRepository.getTheme()
        
        // 3. Apply the theme BEFORE super.onCreate
        setTheme(savedTheme)
        
        // 4. Proceed with normal lifecycle
        super.onCreate(savedInstanceState)
    }
}
