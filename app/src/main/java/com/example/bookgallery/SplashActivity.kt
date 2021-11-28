package com.example.bookgallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bookgallery.databinding.ActivitySplashBinding
import com.example.bookgallery.repositories.FlickrApiService
import com.example.bookgallery.repositories.RoomServiceRepository

class SplashActivity : AppCompatActivity() {

        private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RoomServiceRepository.init(this)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        },3000)

        startActivity(intent)

    }
}