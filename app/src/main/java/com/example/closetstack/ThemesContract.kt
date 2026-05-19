package com.example.closetstack

interface ThemesContract {
    interface View {
        fun applyThemeAndRestart()
    }
    interface Presenter {
        fun onThemeSelected(themeResId: Int)
    }
}
