package com.example.bookgallery.api

import com.example.bookgallery.datamodels.PhotoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IFlickrInterface {
    @GET("/services/rest/?method=flickr.photos.search&api_key=83de65d98fa4f7eb77a44262cefb3612&format=json&extras=url_s&nojsoncallback=1&media=photos&content_type=1")
   suspend fun getPhotos(
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("page") page: Int
    ): Response<PhotoModel>
}



