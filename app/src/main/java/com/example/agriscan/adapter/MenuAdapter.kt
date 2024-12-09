package com.example.agriscan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agriscan.data.model.MenuItem
import com.example.agriscan.R
import com.example.agriscan.databinding.ItemMenuBinding

class MenuAdapter(
    private val menuList: List<MenuItem>,
    private val onMenuItemClickListener: OnMenuItemClickListener
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    interface OnMenuItemClickListener {
        fun onMenuItemClick(menuItem: MenuItem)
    }

    inner class MenuViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menuItem: MenuItem) {
            binding.tvMenuName.text = menuItem.name
            binding.ivMenuIcon.setImageResource(menuItem.icon)
            binding.cvMenuItem.setBackgroundResource(R.drawable.bg_menu)

            binding.root.setOnClickListener {
                onMenuItemClickListener.onMenuItemClick(menuItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
        applyItemAnimation(holder.itemView)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onViewAttachedToWindow(holder: MenuViewHolder) {
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