package com.example.agriscan.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class ImageUploadRequest(
    val image: String // Base64 string
)

data class ImageUploadResponse(
    val result: String
)

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("predicts")
    fun uploadImage(@Body request: ImageUploadRequest): Call<ImageUploadResponse>

//    @Multipart
//    @POST("predicts")
//    fun uploadImage(
//        @Part image: MultipartBody.Part,
//        @Part("image_base64") base64Image: RequestBody
//    ): Call<ImageUploadResponse>
}
