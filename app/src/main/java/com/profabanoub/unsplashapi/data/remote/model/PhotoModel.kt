package com.profabanoub.unsplashapi.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoModel(
    val id: String,
    val description: String?,
    val urls: PhotoUrlsModel,
    val user: UnsplashUserModel,
) : Parcelable {

    @Parcelize
    data class PhotoUrlsModel(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable

    @Parcelize
    data class UnsplashUserModel(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchAPI&utm_medium=referral"
    }
}
