package com.example.agriscan.ui.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agriscan.adapter.ExampleAdapter
import com.example.agriscan.data.repository.ExampleRepository
import com.example.agriscan.databinding.ActivityExampleBinding

class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exampleRepository = ExampleRepository()
        val plantExamples = exampleRepository.getPlantExamples()

        val adapter = ExampleAdapter(this, plantExamples)
        binding.rvPlantsExample.layoutManager = LinearLayoutManager(this)
        binding.rvPlantsExample.adapter = adapter
    }
}