package com.jdroid.galleryex

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.galleryex.adapter.ImageAdapter
import com.jdroid.galleryex.databinding.ActivityGalleryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityGalleryBinding.inflate(layoutInflater) }
    private val imageList = ArrayList<Uri>()
    private var adapter: ImageAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initLayout()
        initData()
        initListener()
    }

    private fun initData() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID), null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC")

                cursor?.let {
                    val columnsIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    while (it.moveToNext()) {
                        val imageId = it.getLong(columnsIndex)
                        val imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "$imageId")
                        imageList.add(imageUri)
                    }
                }
                cursor?.close()
            }

            setImage()
        }

    }

    private fun initLayout() {
        setContentView(binding.root)

        adapter = ImageAdapter(imageList)
        binding.imageList.adapter = adapter
    }

    private fun initListener() {

    }

    private fun setImage() {
        adapter?.notifyItemRangeChanged(0, imageList.size)
    }
}
