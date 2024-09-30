package com.opsc.onesecondapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MonthImagesAdapter(private var imageItems: List<ImageItem>) : RecyclerView.Adapter<MonthImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = imageItems[position]

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.sample_image)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = imageItems.size

    fun setImages(newItems: List<ImageItem>) {
        imageItems = newItems
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
