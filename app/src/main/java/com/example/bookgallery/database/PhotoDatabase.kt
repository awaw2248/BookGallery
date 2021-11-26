package com.example.bookgallery.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookgallery.datamodels.Photo

@Database(entities = [Photo:: class], version = 1 )
abstract  class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao() : PhotoDao

}
