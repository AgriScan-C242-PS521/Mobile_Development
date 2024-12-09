package com.example.agriscan.data.repository

import com.example.agriscan.R
import com.example.agriscan.data.model.MenuItem

class MenuRepository {
    fun getMenus(): List<MenuItem> {
        return listOf(
            MenuItem("Contoh", R.drawable.ic_example),
            MenuItem("Riwayat", R.drawable.ic_history),
            MenuItem("Pengaturan", R.drawable.ic_setting),
            MenuItem("Keluar", R.drawable.ic_sign_out)
        )
    }
}
