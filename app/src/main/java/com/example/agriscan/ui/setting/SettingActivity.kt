package com.example.agriscan.ui.setting

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.agriscan.databinding.ActivitySettingBinding
import java.io.ByteArrayOutputStream

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    private val viewModel: SettingsViewModel by viewModels { SettingsViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isDarkModeLiveData.observe(this) { isDarkMode ->
            binding.switchTheme.isChecked = isDarkMode
            applyTheme(isDarkMode)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
        }

    }

    private fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
