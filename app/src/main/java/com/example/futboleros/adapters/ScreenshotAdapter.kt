package com.example.futboleros.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.futboleros.databinding.ItemScreenshotBinding
import com.example.futboleros.utils.ImageUtils

class ScreenshotAdapter(
    private val screenshots: List<String>,
    private val onScreenshotClick: ((String) -> Unit)? = null
) : RecyclerView.Adapter<ScreenshotAdapter.ScreenshotViewHolder>() {

    class ScreenshotViewHolder(val binding: ItemScreenshotBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotViewHolder {
        val binding =
            ItemScreenshotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScreenshotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenshotViewHolder, position: Int) {
        val imageUrl = screenshots[position]
        ImageUtils.loadImage(holder.binding.ivScreenshot, imageUrl)

        holder.itemView.setOnClickListener {
            onScreenshotClick?.invoke(imageUrl)
        }
    }

    override fun getItemCount(): Int = screenshots.size
}
