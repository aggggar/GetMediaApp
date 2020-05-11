package com.example.getmediaapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.getmediaapp.R
import com.example.getmediaapp.ui.get_cont.SetCountDialogFragment
import com.example.getmediaapp.utils.Extensions.logE
import com.example.getmediaapp.utils.Extensions.toast
import com.example.getmediaapp.utils.RealPathUtils
import com.example.getmediaapp.utils.showToast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainActivity : AppCompatActivity(), SetCountDialogFragment.SetCount{

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
        private const val GET_AUDIO_CODE = 110
        private const val GET_IMAGE_CODE = 111
        private const val GET_VIDEO_CODE = 112
    }

    private val TAG = MainActivity::class.simpleName
    private lateinit var mainViewModel : MainViewModel
    private lateinit var count: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        SetCountDialogFragment().show(supportFragmentManager, "")
        btnAdd.setOnClickListener {
            if (!TextUtils.isEmpty(count)) {
                pickImageIntent()
            } else {
                toast("Specify count")
            }
        }
//
//        btn_pick_video.setOnClickListener {
//            val intent = Intent()
//            intent.type = "video/*"
//            intent.action = Intent.ACTION_OPEN_DOCUMENT
//            startActivityForResult(/*Intent.createChooser(intent, "Select Video")*/intent , GET_VIDEO_CODE)
//        }
//
//        btn_pick_audio.setOnClickListener {
//            val intent = Intent()
//            intent.type = "audio/*"
//            intent.action = Intent.ACTION_OPEN_DOCUMENT
//            startActivityForResult(Intent.createChooser(intent, "Select Audio"), GET_AUDIO_CODE)
//        }
//
//        btnUploadFile.setOnClickListener {
//            if (!TextUtils.isEmpty(tvRealPath.text)){
//                uploadFile(tvRealPath.text.toString())
//            }
//        }
    }

    private fun pickImageIntent() {
        CropImage.activity(null)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .start(this)
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_OPEN_DOCUMENT
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_IMAGE_CODE)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            "tff".showToast(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GET_IMAGE_CODE -> {
                Log.e(TAG, data?.data.toString())
                val imageRealPath = data?.data?.let { RealPathUtils.getRealPathFromUri(this, it) }
                Log.e(TAG, "Real Path: $imageRealPath")
                CropImage.activity(Uri.parse(imageRealPath))
                    .start(this)
            }
        }


        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                toast("Cropping successful, Sample: " + result.sampleSize)
                logE(result.uri.toString())
                val realPath= RealPathUtils.getRealPathFromUri(this, result.uri)
                logE(realPath.toString())

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                toast("Cropping failed: " + result.error)
            }
        }
    }

    override fun setCount(count: String) {
        this.count = count
    }


}
