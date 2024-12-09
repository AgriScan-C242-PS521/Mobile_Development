package com.example.agriscan.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class ImageUploadResponse(
    val top_predictions: List<Prediction>
)

data class Prediction(
    val confidence: Double,
    val label: String
)

interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(
        @Part file: MultipartBody.Part
    ): Call<ImageUploadResponse>
}