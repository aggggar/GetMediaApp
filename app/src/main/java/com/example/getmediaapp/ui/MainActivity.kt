package com.example.getmediaapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.getmediaapp.R
import com.example.getmediaapp.utils.RealPathUtils
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainActivity : AppCompatActivity() {


    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
        private const val GET_AUDIO_CODE = 110
        private const val GET_IMAGE_CODE = 111
        private const val GET_VIDEO_CODE = 112
    }

    private val TAG = MainActivity::class.simpleName
    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()

         mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        btn_pick_image.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_IMAGE_CODE)
        }

        btn_pick_video.setOnClickListener{
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(/*Intent.createChooser(intent, "Select Video")*/intent , GET_VIDEO_CODE)
        }

        btn_pick_audio.setOnClickListener {
            val intent = Intent()
            intent.type = "audio/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, "Select Audio"), GET_AUDIO_CODE)
        }

        btnUploadFile.setOnClickListener {
            if (!TextUtils.isEmpty(tvRealPath.text)){
                uploadFile(tvRealPath.text.toString())
            }
        }
    }

    private fun uploadFile(realPath: String) {
        val file = File(realPath)
        val requestFile = file.asRequestBody(contentResolver.getType(Uri.parse(realPath))?.let { it.toMediaTypeOrNull() })
        val descriptionText = "this is the description"
        val fileBody = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val description = RequestBody.create(MultipartBody.FORM, descriptionText)
        mainViewModel.uploadFile(description, fileBody)
    }

    private fun getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                }

                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Companion.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GET_IMAGE_CODE -> {
                Log.e(TAG, data?.data.toString())
                val imageRealPath = data?.data?.let { RealPathUtils.getRealPathFromUri(this, it) }
                Log.e(TAG, "Real Path: $imageRealPath")
                tvRealPath.text = imageRealPath
            }
            GET_VIDEO_CODE -> {
                Log.e(TAG, data?.data.toString())
                val videoRealPath = data?.data?.let { RealPathUtils.getRealPathFromUri(this, it) }
                Log.e(TAG, "Real Path: $videoRealPath")
                tvRealPath.text = videoRealPath
            }
            GET_AUDIO_CODE -> {
                Log.e(TAG, data?.data.toString())
                val audioRealPath = data?.data?.let { RealPathUtils.getRealPathFromUri(this, it) }
                Log.e(TAG, "Real Path: $audioRealPath")
                tvRealPath.text = audioRealPath
            }
        }
    }




}
