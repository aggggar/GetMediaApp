package com.example.getmediaapp.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.getmediaapp.BuildConfig

object Extensions {
    fun Context.toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun Context.logD(message: String){
        if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, message)
    }

    fun Context.logE(message: String){
        if (BuildConfig.DEBUG) Log.e(this::class.java.simpleName, message)
    }
}