package com.example.bookgallery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookgallery.datamodels.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosViewModel:ViewModel() {

    val photosLiveData = MutableLiveData<List<Photo>>()
    fun getPhotos(lon:Double,lat:Double,page:Int) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}