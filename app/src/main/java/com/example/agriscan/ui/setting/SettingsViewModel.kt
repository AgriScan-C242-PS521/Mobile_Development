package com.example.agriscan.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.agriscan.data.repository.SettingsRepository

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    val isDarkModeLiveData: LiveData<Boolean> = repository.isDarkModeLiveData

    fun toggleTheme(isDarkMode: Boolean) {
        repository.setDarkMode(isDarkMode)
    }
}
