package com.jdroid.glideex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.jdroid.glideex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Glide.with(this)
            .load("https://blog.kakaocdn.net/dn/b7gKAc/btrF3gQobX4/uMdhkiG6GbnNplDGgLgWKK/img.gif")
            .into(binding.imageView)
    }
}