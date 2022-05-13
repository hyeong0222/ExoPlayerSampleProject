package com.example.exoplayersampleproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader

class MainActivity : AppCompatActivity() {

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var fetchVideo: ActivityResultLauncher<Intent>
    private lateinit var takeVideo: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1 = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)

        fetchVideo = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                with (it.data?.data) {
                    if (this != null) goToSecondActivity(this)
                }
            }
        }

        takeVideo = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                with (it.data?.data) {
                    if (this != null) goToSecondActivity(this)
                }
            }
        }

        button1.setOnClickListener {
            val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "video/"
                fetchVideo.launch(intent)
            }
        }

        button2.setOnClickListener {
            val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    takeVideo.launch(intent)
                }
            }
        }
    }

    private fun getRealPathFromUri(uri: Uri): String {
        val proj = arrayListOf(MediaStore.Images.Media.DATA).toTypedArray()
        val loader = CursorLoader(this, uri, proj, null, null, null)
        val cursor = loader.loadInBackground()

        var result = ""
        cursor?.let {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            result = it.getString(columnIndex)
            it.close()

        }
        return result
    }

    private fun goToSecondActivity(uri: Uri) {
        val path = getRealPathFromUri(uri)

        if (uri.toString().contains("image") || uri.toString().contains("video")) {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("uri", path)
            startActivity(intent)
        }
    }
}