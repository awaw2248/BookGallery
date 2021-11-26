package com.example.bookgallery.repositories

import android.content.Context
import androidx.room.Room
import com.example.bookgallery.database.PhotoDao
import com.example.bookgallery.database.PhotoDatabase
import com.example.bookgallery.datamodels.Photo

private const val DATABASE_NAME = "photos_database"
class RoomServiceRepository(context: Context) {

//__________________database_____________________________________
    private val database = Room.databaseBuilder(context,
        PhotoDatabase :: class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration().build()
//________________________________________________________________


//___________________________photosDao____________________________
    private val photosDao = database.photoDao()
    suspend fun insertPhotos(photos: List<Photo>)= photosDao.insertPhotos(photos)
    suspend fun getPhoto() = photosDao.getPhotos()

//________________________________________________________________


    companion object {
        private var instance: RoomServiceRepository? = null

        fun init(context: Context) {
            if (instance == null)
                instance = RoomServiceRepository(context)
        }

        fun get(): RoomServiceRepository {
            return instance ?: throw Exception("Room Service Repository must be initialized")
        }
    }




}