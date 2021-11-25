package com.example.bookgallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookgallery.repositories.RoomServiceRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RoomServiceRepository.init(this)
    }

}