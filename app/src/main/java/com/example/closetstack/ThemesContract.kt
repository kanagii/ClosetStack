package com.example.closetstack

interface ThemesContract {
    interface View {
        fun applyThemeAndRestart()
        fun showCustomThemeModal()
    }

    interface Presenter {
        fun onThemeSelected(themeResId: Int)
        fun onCustomThemeClicked()
    }
}