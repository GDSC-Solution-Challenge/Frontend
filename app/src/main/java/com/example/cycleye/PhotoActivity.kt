package com.example.cycleye

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.cycleye.databinding.ActivityPhotoBinding
import java.io.File
import java.text.SimpleDateFormat

class PhotoActivity : AppCompatActivity() {

    // constant
    private val PERMISSION_REQUEST = 100
    private val AUTHORITIES = "com.example.cycleye.fileprovider"

    //bind
    private val binding get() = photoBinding!!
    private var photoBinding : ActivityPhotoBinding? = null

    private lateinit var f: File;
    private var activityResultLauncher :  ActivityResultLauncher<Intent>? = null
    private var saveUri: Uri? = null

    // permissions
    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoBinding = ActivityPhotoBinding.inflate(layoutInflater)

        // launcher
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                BitmapFactory.decodeFile(f.absolutePath)?.let { image ->
                    binding.imageView.setImageBitmap(image)
                }
            }
        }

        // Camera Btn Click
        binding.cameraBtn.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = File(
                File("${filesDir}/image").apply {
                    if(!this.exists()){
                        this.mkdirs()
                    }
                }, makeFileName()
            )
            saveUri = FileProvider.getUriForFile(
                this,
                AUTHORITIES,
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri)
            f = photoFile
            activityResultLauncher!!.launch(intent)
        }

        setContentView(binding.root)
        checkPermissons(PERMISSIONS, PERMISSION_REQUEST)
    }

    private fun checkPermissons(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList: MutableList<String> = mutableListOf()
        for(permission in permissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission)
            }
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSION_REQUEST)
            return false
        }
        return true
    }

    //카메라 권한 검사 후 callback 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 승인이 필요합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun makeFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }
}