package de.rauschdo.photoapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MarshMallowPermission(var activity: Activity) {

    // CHECK PERMISSIONS -------------------------------------------------------------------------
    fun checkPermissionForLocation(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForRecord(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForReadExternalStorage(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForWriteExternalStorage(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForCamera(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }

    // REQUEST PERMISSIONS -------------------------------------------------------------------------
    fun requestPermissionForLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                activity,
                "Location permission needed. Please allow in App Settings for additional functionality.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    fun requestPermissionForRecord() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            Toast.makeText(
                activity,
                "Microphone permission needed for recording. Please allow in App Settings for additional functionality.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                2
            )
        }
    }

    fun requestPermissionForReadExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                activity,
                "External Storage permission needed. Please allow in App Settings for additional functionality.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                3
            )
        }
    }

    fun requestPermissionForWriteExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                activity,
                "External Storage permission needed. Please allow in App Settings for additional functionality.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                4
            )
        }
    }

    fun requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            )
        ) {
            Toast.makeText(
                activity,
                "Camera permission needed. Please allow in App Settings for additional functionality.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 5)
        }
    }
} //@end
