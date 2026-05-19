package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.view.View

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Semi-functional as requested: Only the Themes button works
        findViewById<View>(R.id.btnNavigateThemes).setOnClickListener {
            startActivity(Intent(this, ThemesActivity::class.java))
        }
    }
}
