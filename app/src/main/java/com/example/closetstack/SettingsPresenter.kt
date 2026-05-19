package com.example.closetstack

class SettingsPresenter(private val view: SettingsContract.View) : SettingsContract.Presenter {
    override fun onThemesButtonClicked() {
        view.navigateToThemes()
    }
}