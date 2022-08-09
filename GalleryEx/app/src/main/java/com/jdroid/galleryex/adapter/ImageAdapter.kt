package com.jdroid.galleryex.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.galleryex.databinding.ViewImageItemsBinding

class ImageAdapter(private val uriList: ArrayList<Uri>): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ViewImageItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = uriList.size

    inner class ViewHolder(private val binding: ViewImageItemsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.imageView.setImageURI(uriList[adapterPosition])
        }
    }
}