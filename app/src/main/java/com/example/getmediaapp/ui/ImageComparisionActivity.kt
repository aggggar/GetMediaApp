package com.example.getmediaapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import com.bumptech.glide.Glide
import com.example.getmediaapp.R
import com.example.getmediaapp.utils.FileUtil
import kotlinx.android.synthetic.main.activity_image_comparision.*
import java.io.File

class ImageComparisionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_comparision)

        val actualFile : File = intent.extras?.get("actual") as File
        val compressedFile : File = intent.extras?.get("compressed") as File

        tvActual.text = "Actual "+FileUtil.fileSizeInKb(actualFile)
        tvCompressed.text = "Copmressed "+FileUtil.fileSizeInKb(compressedFile)

        Glide.with(this)
            .load(actualFile.path)
            .into(ivActualImage)

        Glide.with(this)
            .load(compressedFile.path)
            .into(ivCompressedImage)
    }
}
