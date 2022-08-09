package com.jdroid.galleryex

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.galleryex.adapter.ImageAdapter
import com.jdroid.galleryex.databinding.ActivityGalleryBinding

class GalleryActivity: AppCompatActivity() {

    private val binding by lazy { ActivityGalleryBinding.inflate(layoutInflater) }
    private val imageUriList = ArrayList<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initLayout()
        initListener()
    }

    private fun initData() {
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID), null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC")

        cursor?.let {
            val columnsIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while(it.moveToNext()) {
                val imageId = it.getLong(columnsIndex)
                imageUriList.add(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "$imageId"))
                Log.d("imageUri", "$imageId")
            }
        }
        cursor?.close()
    }

    private fun initLayout() {
        setContentView(binding.root)

        Log.d("imageUriList", "$imageUriList")
        binding.imageList.adapter = ImageAdapter(imageUriList)
    }

    private fun initListener() {

    }
}
