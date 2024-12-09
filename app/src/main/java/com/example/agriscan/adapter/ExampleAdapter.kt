package com.example.agriscan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agriscan.data.model.ExampleItem
import com.example.agriscan.databinding.ItemPlantExampleBinding

class ExampleAdapter(
    private val context: Context,
    private val plantExamples: List<ExampleItem>
) : RecyclerView.Adapter<ExampleAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPlantExampleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlantExampleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = plantExamples[position]
        holder.binding.tvPlantName.text = plant.name
        holder.binding.vpDiseases.adapter = DiseaseAdapter(context, plant.diseases)
    }

    override fun getItemCount(): Int = plantExamples.size
}