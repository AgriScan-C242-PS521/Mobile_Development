package com.example.agriscan.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agriscan.R
import com.example.agriscan.databinding.ItemHistoryBinding
import com.example.agriscan.room.History

class HistoryAdapter(
    private val list: List<History>,
    private val onDeleteClick: (History) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            tvPlant.text = item.plant
            tvCondition.text = item.condition
            "Confidence: ${item.confidence}%".also { tvConfidence.text = it }
            "Media: ${item.media}".also { tvMedia.text = it }
            "Tanggal: ${item.dateTime}".also { tvDate.text = it }
            Glide.with(ivImage.context)
                .load(item.imagePath)
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivImage)

            ivImage.setOnClickListener {
                showFullImageDialog(item.imagePath, ivImage)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun showFullImageDialog(imagePath: String, imageView: ImageView) {
        val dialog = Dialog(imageView.context)
        dialog.setContentView(R.layout.dialog_full_image)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val fullImageView = dialog.findViewById<ImageView>(R.id.ivFullImage)

        Glide.with(fullImageView.context)
            .load(imagePath)
            .placeholder(R.drawable.ic_launcher_background)
            .into(fullImageView)

        dialog.show()
    }
}