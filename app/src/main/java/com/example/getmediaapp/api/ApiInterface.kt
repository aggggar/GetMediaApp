package com.example.getmediaapp.api

import com.example.getmediaapp.model.ImageUploadResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {

    @Multipart
    @POST("")
    fun uploadFile(@Part("description") description : RequestBody,
                   @Part file : MultipartBody.Part) : Call<ImageUploadResponse>

    @Multipart
    @POST("")
    fun uploadFileRx(@Part("description") description : RequestBody,
                   @Part file : MultipartBody.Part) : Single<ImageUploadResponse>
}