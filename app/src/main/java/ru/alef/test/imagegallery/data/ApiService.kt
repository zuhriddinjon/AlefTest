package ru.alef.test.imagegallery.data

import ru.alef.test.imagegallery.common.Constants
import retrofit2.http.GET

interface ApiService {

    @GET(Constants.SOURCE_URL)
    suspend fun getPhotoList(): List<String>
}