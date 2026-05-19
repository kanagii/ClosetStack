package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.system.exitProcess

class ThemesActivity : BaseActivity(), ThemesContract.View {

    private lateinit var presenter: ThemesContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)

        presenter = ThemesPresenter(this, themeRepository)

        // Bind Buttons
        findViewById<Button>(R.id.btnThemeOriginal).setOnClickListener {
            presenter.onThemeSelected(R.style.Theme_ClosetStack_OriginalNavy)
        }

        findViewById<Button>(R.id.btnThemeDefaultDark).setOnClickListener {
            presenter.onThemeSelected(R.style.Theme_ClosetStack_DefaultDark)
        }

        findViewById<Button>(R.id.btnThemeBubbleGum).setOnClickListener {
            presenter.onThemeSelected(R.style.Theme_ClosetStack_BubbleGum)
        }

        findViewById<Button>(R.id.btnThemeCustom).setOnClickListener {
            presenter.onCustomThemeClicked()
        }
    }

    override fun applyThemeAndRestart() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Restart Required")
            .setMessage("To apply the new theme, ClosetStack needs to restart.")
            .setPositiveButton("Restart") { _, _ ->
                // Restart to HomeActivity instead of the empty MainActivity
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()
                exitProcess(0)
            }
            .setNegativeButton("Later", null)
            .show()
    }

    override fun showCustomThemeModal() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_color_picker, null)
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .create()

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvColorPickerTitle)
        tvTitle?.text = "Design Your Vibe"

        dialogView.findViewById<Button>(R.id.btnCancelColor).setOnClickListener { dialog.dismiss() }
        dialogView.findViewById<Button>(R.id.btnConfirmColor).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}