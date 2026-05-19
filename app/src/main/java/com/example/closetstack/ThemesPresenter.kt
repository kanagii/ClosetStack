package com.example.closetstack

// 2. The Presenter (The Logic)
class ThemesPresenter(
    private val view: ThemesContract.View,
    private val repository: ThemeRepository
) : ThemesContract.Presenter {

    override fun onThemeSelected(themeResId: Int) {
        // Save the new state to the Model
        repository.saveTheme(themeResId)
        // Instruct the View to react
        view.applyThemeAndRestart()
    }
    // Add this implementation inside your ThemesPresenter class
    override fun onCustomThemeClicked() {
        // The presenter decides it's time to show the modal
        view.showCustomThemeModal()
    }
}
