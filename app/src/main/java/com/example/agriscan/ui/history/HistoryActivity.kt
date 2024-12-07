package com.example.agriscan.ui.history

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agriscan.adapter.HistoryAdapter
import com.example.agriscan.databinding.ActivityHistoryBinding
import com.example.agriscan.room.AppDatabase
import com.example.agriscan.room.History
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        setupRecyclerView()
        loadHistoryData()
    }

    private fun setupRecyclerView() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun loadHistoryData() {
        lifecycleScope.launch {
            val historyList = db.historyDao().getAll()
            adapter = HistoryAdapter(historyList) { history ->
                deleteHistory(history)
            }
            binding.rvHistory.adapter = adapter
        }
    }

    private fun deleteHistory(history: History) {
        lifecycleScope.launch {
            db.historyDao().delete(history)
            Toast.makeText(this@HistoryActivity, "History deleted!", Toast.LENGTH_SHORT).show()
            loadHistoryData() // Refresh data
        }
    }
}

