package com.profabanoub.unsplashapi.data.remote.api

import com.profabanoub.unsplashapi.data.remote.model.PhotoModel

data class UnsplashResponse(
    val results: List<PhotoModel>
)