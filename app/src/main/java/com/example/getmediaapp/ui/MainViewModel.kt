package com.example.getmediaapp.ui

import androidx.lifecycle.ViewModel
import com.example.getmediaapp.api.ApiClient
import com.example.getmediaapp.model.ImageUploadResponse
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    fun uploadFile(description : RequestBody,
                   file : MultipartBody.Part){

        ApiClient.getApiInterface().uploadFile(description, file).enqueue(object : Callback<ImageUploadResponse> {
            override fun onResponse(call: Call<ImageUploadResponse>, response: Response<ImageUploadResponse>) {

            }

            override fun onFailure(call: Call<ImageUploadResponse>, t: Throwable) {

            }
        })
    }

    fun uploadFileRx(description: RequestBody,
                     file: MultipartBody.Part){

        ApiClient.getApiInterface().uploadFileRx(description, file)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object: SingleObserver<ImageUploadResponse> {
                override fun onSuccess(t: ImageUploadResponse) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onSubscribe(d: Disposable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onError(e: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.isDisposed
    }


}