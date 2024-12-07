package com.example.agriscan.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: History)

    @Query("SELECT * FROM history ORDER BY id DESC")
    suspend fun getAll(): List<History>

    @Delete
    suspend fun delete(history: History)
}
