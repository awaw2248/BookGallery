package com.example.bookgallery.repositories

import com.example.bookgallery.api.IFlickrInterface
import com.example.bookgallery.datamodels.PhotoModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FlickrApiService {
    private val retrofit = Retrofit.Builder().baseUrl("http://api.flickr.com")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val flickrApi = retrofit.create(IFlickrInterface::class.java)
    suspend fun getPhotos(lon:Double,lat:Double,page:Int): Response<PhotoModel> =
        flickrApi.getPhotos(lon,lat,page)
}
