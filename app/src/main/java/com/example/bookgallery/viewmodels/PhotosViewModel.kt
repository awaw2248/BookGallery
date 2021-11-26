package com.example.bookgallery.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookgallery.datamodels.Photo
import com.example.bookgallery.repositories.FlickrApiService
import com.example.bookgallery.repositories.RoomServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "PhotosViewModel"

class PhotosViewModel : ViewModel() {

    private val databaseRepo = RoomServiceRepository.get()

    val photosLiveData = MutableLiveData<List<Photo>>()
    val errorLiveData = MutableLiveData<String>()
    val permission = MutableLiveData<Boolean>()
    fun getPhotos(lon: Double, lat: Double, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val photoResponse = FlickrApiService.getPhotos(lon, lat, page)
                if (photoResponse.isSuccessful) {
                    photoResponse.body()?.let {
                        val listOfPhotos = it.photos.photo
                        if (listOfPhotos.isNotEmpty()) {
                            Log.d(TAG, it.photos.photo.toString())
                            photosLiveData.postValue(listOfPhotos)
                            databaseRepo.insertPhotos(listOfPhotos)
                        }
                    }
                } else {
                    Log.d(TAG, photoResponse.message())
                    errorLiveData.postValue("Error while retrieving data")
                    photosLiveData.postValue(databaseRepo.getPhoto())
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                errorLiveData.postValue("Could not connect to server")
                photosLiveData.postValue(databaseRepo.getPhoto())
            }
        }
    }
}
