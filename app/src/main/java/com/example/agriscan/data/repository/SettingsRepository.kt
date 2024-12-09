package com.example.agriscan.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingsRepository(context: Context) {

    companion object {
        private const val PREFS_NAME = "theme_prefs"
        private const val PREFS_THEME_KEY = "isDarkMode"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isDarkModeLiveData = MutableLiveData<Boolean>()
    val isDarkModeLiveData: LiveData<Boolean> get() = _isDarkModeLiveData

    init {
        _isDarkModeLiveData.value = sharedPreferences.getBoolean(PREFS_THEME_KEY, false)
    }

    fun setDarkMode(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean(PREFS_THEME_KEY, isDarkMode).apply()
        _isDarkModeLiveData.value = isDarkMode
    }
}