package com.example.agriscan.data.repository

import com.example.agriscan.R
import com.example.agriscan.data.model.PlantItem

class PlantRepository {
    fun getPlants(): List<PlantItem> {
        return listOf(
            PlantItem("Tomat", R.drawable.bg_tomato, R.drawable.tomato),
            PlantItem("Jagung", R.drawable.bg_corn, R.drawable.corn),
            PlantItem("Kentang", R.drawable.bg_potato, R.drawable.potato)
        )
    }
}
