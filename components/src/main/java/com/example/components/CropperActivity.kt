package com.example.components

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.components.Fragments.SignUp4Fragment.Companion.imageUri
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

class CropperActivity : AppCompatActivity() {

    lateinit var destUri: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cropper)
        val options: UCrop.Options = UCrop.Options()
        options.setMaxBitmapSize(500)

        destUri = java.lang.StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
        UCrop.of(imageUri, Uri.fromFile(File(cacheDir, destUri))).withOptions(options)
            .withAspectRatio(0F, 0F).useSourceImageAspectRatio().withMaxResultSize(1000, 1000)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            imageUri = data?.let { UCrop.getOutput(it) }!!
            setResult(-1)
            finish()
        } else {

        }
    }

}