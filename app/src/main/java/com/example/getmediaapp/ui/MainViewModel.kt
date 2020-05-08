package com.example.getmediaapp.ui

import androidx.lifecycle.ViewModel
import com.example.getmediaapp.api.ApiClient
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {


    fun uploadFile(description : RequestBody,
                   file : MultipartBody.Part){

        ApiClient.getApiInerface().uploadFile(description, file).enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

        })
    }


}