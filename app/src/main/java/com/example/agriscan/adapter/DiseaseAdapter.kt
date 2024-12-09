package com.example.agriscan.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agriscan.BuildConfig
import com.example.agriscan.data.model.DiseaseItem
import com.example.agriscan.databinding.ItemDiseaseBinding
import com.example.agriscan.databinding.DialogFullImageBinding

class DiseaseAdapter(
    private val context: Context,
    private val diseases: List<DiseaseItem>
) : RecyclerView.Adapter<DiseaseAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiseaseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disease = diseases[position]
        holder.binding.tvDiseaseName.text = disease.name
        holder.binding.tvDiseaseSolution.text = disease.solution
        holder.binding.ivDiseaseImage.setImageResource(disease.imageRes)

        holder.binding.ivDiseaseImage.setOnClickListener {
            showFullImageDialog(disease.imageRes)
        }

        if (disease.searchKeyword.isBlank()) {
            holder.binding.btnBuyMedicine.visibility = View.GONE
        } else {
            holder.binding.btnBuyMedicine.visibility = View.VISIBLE
            holder.binding.btnBuyMedicine.setOnClickListener {
                openMarketplaceSearch(disease.searchKeyword)
            }
        }
    }

    override fun getItemCount(): Int = diseases.size

    private fun showFullImageDialog(imageRes: Int) {
        val dialog = Dialog(context)
        val dialogBinding = DialogFullImageBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.ivFullImage.setImageResource(imageRes)
        dialogBinding.ivFullImage.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openMarketplaceSearch(keyword: String) {
        val searchUrl = "${BuildConfig.TOKOPEDIA_URL}search?st=product&q=${Uri.encode(keyword)}"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(searchUrl)
        }
        context.startActivity(intent)
    }
}