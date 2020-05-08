package com.example.getmediaapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ImageUploadResponse(@Json(name = "status") val status: String,
                               @Json(name = "message") val message: String)