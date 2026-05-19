package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.widget.Button

// 3. The View (The UI Binding)
class ThemesActivity : BaseActivity(), ThemesContract.View {

    private lateinit var presenter: ThemesContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // BaseActivity applies the theme here!
        setContentView(R.layout.activity_themes)

        // Initialize Presenter
        presenter = ThemesPresenter(this, themeRepository)

        // Bind Buttons
        findViewById<Button>(R.id.btnThemePaperZine).setOnClickListener {
            presenter.onThemeSelected(R.style.Theme_ClosetStack_PaperZine)
        }
        
        findViewById<Button>(R.id.btnThemeStreetHeat).setOnClickListener {
            presenter.onThemeSelected(R.style.Theme_ClosetStack_StreetHeat)
        }
        
        findViewById<Button>(R.id.btnThemeMidnight).setOnClickListener {
            presenter.onThemeSelected(R.style.Theme_ClosetStack_Midnight)
        }
        
        findViewById<Button>(R.id.btnThemeBubbleGum).setOnClickListener {
            presenter.onThemeSelected(R.style.Theme_ClosetStack_BubbleGum)
        }
    }

    override fun applyThemeAndRestart() {
        // Invalidate the current UI and restart the activity stack to apply the new theme globally
        val intent = Intent(this, MainActivity::class.java) // Redirect to your app's main entry point
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        
        // Alternative for just this screen: recreate()
    }
}
