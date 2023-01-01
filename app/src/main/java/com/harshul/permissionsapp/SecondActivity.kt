package com.harshul.permissionsapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class SecondActivity : AppCompatActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadPermission = false
    private var isLocationPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                Log.d("TAG", "onCreate: ")
                isReadPermission = permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: isReadPermission
                isLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
                    ?: isLocationPermission

                // if (!isReadPermission || !isLocationPermission) requestPermissions()

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.d("TAG", "Read Rational: TRUE")
                } else Log.d("TAG", "Read Rational: FALSE")

                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Log.d("TAG", "Location Rational: TRUE")
                } else Log.d("TAG", "Location Rational: FALSE")

            }
    }

    private fun requestPermissions() {
        isReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        //val permissionRequest: MutableList<String> = ArrayList()
        val permissionRequest = mutableListOf<String>()


        if (!isReadPermission) permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!isLocationPermission) permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionRequest.isNotEmpty()) permissionLauncher.launch(permissionRequest.toTypedArray())

    }
}