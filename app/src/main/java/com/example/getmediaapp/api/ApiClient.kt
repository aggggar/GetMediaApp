package com.example.getmediaapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private var logging   = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    fun getApiInterface():ApiInterface{
        return getRetrofit().create(ApiInterface::class.java)
    }

}