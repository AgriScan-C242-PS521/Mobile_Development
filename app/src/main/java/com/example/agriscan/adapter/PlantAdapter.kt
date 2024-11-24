package com.example.agriscan.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.agriscan.data.model.PlantItem
import com.example.agriscan.ui.scan.ScanActivity
import com.example.agriscan.databinding.ItemPlantBinding

class PlantAdapter(private val context: Context, private val plantList: List<PlantItem>) :
    RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(private val binding: ItemPlantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plantItem: PlantItem) {
            binding.tvPlant.text = plantItem.name
            binding.ivPlant.setImageResource(plantItem.icon)
            binding.cvPlant.setBackgroundResource(plantItem.background)

            binding.root.setOnClickListener {
                val intent = Intent(context, ScanActivity::class.java)
                intent.putExtra("PLANT_NAME", plantItem.name)

                context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity).toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plantList[position])
        applyItemAnimation(holder.itemView)
    }

    override fun getItemCount(): Int = plantList.size

    override fun onViewAttachedToWindow(holder: PlantViewHolder) {
        super.onViewAttachedToWindow(holder)
        applyItemAnimation(holder.itemView)
    }
    private fun applyItemAnimation(view: View, duration: Long = 500) {
        view.alpha = 0f
        view.translationY = 100f
        view.scaleX = 0.8f
        view.scaleY = 0.8f

        view.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(duration)
            .start()
    }
}
