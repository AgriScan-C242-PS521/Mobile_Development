package com.example.agriscan.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: String,
    val plant: String,
    val condition: String,
    val confidence: String,
    val media: String,
    val imagePath: String
)