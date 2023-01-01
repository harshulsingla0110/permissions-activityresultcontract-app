package com.harshul.permissionsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.harshul.permissionsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { grantResult ->
            if (grantResult) {
                binding.tvStatus.text = getString(R.string.permission_granted)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    setCameraDenied()
                    binding.tvStatus.text = getString(R.string.permission_denied)
                    showPermissionDeniedDialog()
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CAMERA
                        ) && (AppPreferences.cameraPermissionDeniedOnce)
                    ) {
                        showMandatoryPermissionsNeedDialog()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStatus()

        binding.btnCamera.setOnClickListener { permissionAction() }

    }

    override fun onResume() {
        super.onResume()
        setupStatus()
    }

    private fun setupStatus() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.tvStatus.text = getString(R.string.permission_granted)
        } else if (AppPreferences.cameraPermissionDeniedOnce) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                binding.tvStatus.text = getString(R.string.permission_permanently_denied)
            } else binding.tvStatus.text = getString(R.string.permission_denied)
        } else binding.tvStatus.text = getString(R.string.permission_not_requested)
    }

    private fun permissionAction() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.tvStatus.text = getString(R.string.permission_granted)

        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                //Permission is denied can show some alert here
                showPermissionDeniedDialog()
            } else {
                //ask permission
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun setCameraDenied() {
        AppPreferences.cameraPermissionDeniedOnce = true
    }


    /**
     * We show this custom dialog to alert user denied camera permission
     */
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this).apply {
            setCancelable(true)
            setTitle("Temporary Denied")
            setMessage(getString(R.string.permission_camera_access_required))
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }.show()
    }

    /**
     * We show this custom dialog to alert user that please go to settings to enable camera permission
     */
    private fun showMandatoryPermissionsNeedDialog() {
        AlertDialog.Builder(this).apply {
            setCancelable(true)
            setTitle("Permanently Denied")
            setMessage(getString(R.string.mandatory_permission_access_required))
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }.show()
    }
}