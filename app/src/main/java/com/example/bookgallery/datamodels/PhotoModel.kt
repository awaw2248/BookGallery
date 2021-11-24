package com.example.bookgallery.datamodels


import com.google.gson.annotations.SerializedName

data class PhotoModel(
    @SerializedName("photos")
    val photos: Photos,
    @SerializedName("stat")
    val stat: String
)