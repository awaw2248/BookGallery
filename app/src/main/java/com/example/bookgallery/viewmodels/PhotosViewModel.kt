package com.example.bookgallery.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookgallery.datamodels.Photo
import com.example.bookgallery.repositories.FlickrApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "PhotosViewModel"

class PhotosViewModel : ViewModel() {

    val photosLiveData = MutableLiveData<List<Photo>>()
    val errorLiveData = MutableLiveData<String>()
    fun getPhotos(lon: Double, lat: Double, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val photoResponse = FlickrApiService.getPhotos(lon, lat, page)
                if (photoResponse.isSuccessful) {
                    photoResponse.body()?.let {
                        photosLiveData.postValue(it.photos.photo)
                    }
                } else {
                    Log.d(TAG, photoResponse.message())
                    errorLiveData.postValue(photoResponse.message())
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                errorLiveData.postValue(e.message)
            }
        }
    }
}
