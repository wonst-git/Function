package com.jdroid.galleryex

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.galleryex.adapter.ImageAdapter
import com.jdroid.galleryex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when(v) {
            binding.btnPermission -> {
                requestPermission()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            startGalleryActivity()
        } else {
            if (shouldShowRequestPermissionRationale(permissions[0])) {
                Toast.makeText(this, "권한이 거절되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한을 허용하기 위해 설정으로 이동합니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initLayout()
        initListener()
    }

    private fun initData() {

    }

    private fun initLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startGalleryActivity()
            } else {
                setContentView(binding.root)
            }
        } else {
            startGalleryActivity()
        }
    }

    private fun initListener() {
        binding.btnPermission.setOnClickListener(this)
    }

    private fun startGalleryActivity() {
        startActivity(Intent(this, GalleryActivity::class.java))
        finish()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }
}