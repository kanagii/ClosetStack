package com.example.closetstack

interface SettingsContract {
    interface View {
        fun navigateToThemes()
    }
    
    interface Presenter {
        fun onThemesButtonClicked()
    }
}