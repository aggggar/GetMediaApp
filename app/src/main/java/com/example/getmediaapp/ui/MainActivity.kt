package com.example.getmediaapp.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getmediaapp.R
import com.example.getmediaapp.ui.adapter.ImageAdapter
import com.example.getmediaapp.utils.Extensions.logE
import com.example.getmediaapp.utils.Extensions.toast
import com.example.getmediaapp.utils.FileUtil
import com.example.getmediaapp.utils.RealPathUtils
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(){

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 124
        private const val GET_AUDIO_CODE = 110
        private const val GET_IMAGE_CODE = 111
        private const val GET_VIDEO_CODE = 112
    }

    private val TAG = MainActivity::class.simpleName
    private lateinit var mainViewModel : MainViewModel
    private lateinit var countStr: String
    private var count: Int = 0
    private var recyclerImageCount: Int = 0
    private var actualImage: File? = null
    private var compressedImage: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getReadPermission()
        getWritePermission()


        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvImages.adapter = ImageAdapter()

        handleClicks()

    }

    private fun handleClicks() {

        btnCount.setOnClickListener {
            countStr = etCount.text.toString()
            if (!TextUtils.isEmpty(countStr)){
                tvSelectedCount.text = "Selected count is : $countStr"
                count = countStr.toInt()
                etCount.setText("")
            } else {
                toast("set count")
            }
        }

        btnAdd.setOnClickListener {
            if (count != 0) {
                if (recyclerImageCount < count) {
                    pickImageIntent()
                } else {
                    toast("Able to select only $countStr")
                }
            } else {
                toast("Specify Count")
            }
        }

        btnClearAll.setOnClickListener {
            count = 0
            recyclerImageCount = 0
            tvSelectedCount.text = ""
            (rvImages.adapter as ImageAdapter).clearList()
        }

    }

    private fun pickImageIntent() {
//        CropImage.activity(null)
//            .setGuidelines(CropImageView.Guidelines.ON)
//            .setAspectRatio(1, 1)
//            .start(this)

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_IMAGE_CODE)
    }

    private fun getReadPermission(){
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

    private fun getWritePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                }

                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Companion.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
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

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GET_IMAGE_CODE -> {
                Log.e(TAG, data?.data.toString())
                AlertDialog.Builder(this)
                    .setTitle("Crop")
                    .setMessage("You want to crop image?")
                    .setPositiveButton("yes") { dialog , which ->
                        CropImage.activity(Uri.parse(data?.data.toString()))
                            .start(this)
                    }
                    .setNegativeButton("no") { dialog, which ->
                        actualImage = FileUtil.from(this, data?.data)
                        compressImage()
                        addToRecyclerView(compressedImage?.path)
                        dialog.dismiss()
                    }
                    .show()

            }
            // handle result of CropImageActivity
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    logE(result.uri.toString())
                    actualImage = FileUtil.from(this, result.uri)
                    compressImage()
                    addToRecyclerView(compressedImage?.path)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    logE("Cropping failed: " + result.error)
                }
            }
        }
    }

    private fun compressImage() {
        actualImage?.let {
            lifecycleScope.launch {
               compressedImage = Compressor.compress(this@MainActivity, it, coroutineContext) {
                    resolution(512, 420)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(2_097_152) // 2 MB
                }
            }
        }
    }

    private fun addToRecyclerView(realPath: String?) {
        realPath?.let {
            logE(it)
            (rvImages.adapter as ImageAdapter).addToFirst(it)
            uploadFile(it)
        }
        recyclerImageCount = rvImages.adapter?.itemCount!!
    }


    private fun uploadFile(realPath: String) {
        val file = File(realPath)
        val requestFile = file.asRequestBody(contentResolver.getType(Uri.parse(realPath))?.let { it.toMediaTypeOrNull() })
        val descriptionText = "this is the description"
        val fileBody = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val description = RequestBody.create(MultipartBody.FORM, descriptionText)
        mainViewModel.uploadFile(description, fileBody)
    }


}
