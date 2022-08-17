package com.jdroid.galleryex.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jdroid.galleryex.databinding.ViewImageItemsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.round
import kotlin.math.roundToInt

class ImageAdapter(private val uriList: ArrayList<Uri>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ViewImageItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.measuredWidth)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(uriList[position])
    }

    override fun getItemCount() = uriList.size

    inner class ViewHolder(private val binding: ViewImageItemsBinding, private val parentWidth: Int) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            binding.layout.layoutParams = (binding.layout.layoutParams as ViewGroup.LayoutParams).apply {
                height = parentWidth / 3
            }
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(itemView.context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .thumbnail(Glide.with(itemView.context).asDrawable().load(uri).sizeMultiplier(0.05f))
                    .into(binding.imageView)
            }
        }
    }
}