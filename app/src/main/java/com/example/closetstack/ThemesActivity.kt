package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView // CRITICAL: Added missing type reference
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// 3. The View (The UI Binding)
class ThemesActivity : BaseActivity(), ThemesContract.View {

    private lateinit var presenter: ThemesContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)

        // Initialize Presenter matching our clean contract
        presenter = ThemesPresenter(this, themeRepository)

        // Bind Buttons
        findViewById<Button>(R.id.btnThemeOriginal).setOnClickListener {
            presenter.onThemeSelected(R.style.Theme_ClosetStack_OriginalNavy)
        }

        findViewById<Button>(R.id.btnThemeCustom).setOnClickListener {
            presenter.onCustomThemeClicked()
        }
    }

    override fun applyThemeAndRestart() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun showCustomThemeModal() {
        // 1. Inflate layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_color_picker, null)

        // 2. Build the Material Dialog
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .create()

        // 3. Change title text dynamically (Now compiles perfectly!)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvColorPickerTitle)
        tvTitle?.text = "Design Your Vibe"

        // 4. Bind Custom buttons inside layout
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelColor)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirmColor)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            dialog.dismiss()
        }

        // 5. Render to screen
        dialog.show()
    }
}