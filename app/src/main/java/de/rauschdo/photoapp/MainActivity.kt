package de.rauschdo.photoapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import de.rauschdo.photoapp.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity(), OnRequestPermissionsResultCallback {

    private lateinit var binding: ActivityMainBinding

    /* Variables */
    var mmp = MarshMallowPermission(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(binding.root)
        }

        // Lock to Portrait orientation
        //super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        supportActionBar?.hide()

        with(binding) {
            // MARK: - GALLERY BUTTON
            galleryButt.setOnClickListener {
                if (!mmp.checkPermissionForReadExternalStorage()) {
                    mmp.requestPermissionForReadExternalStorage()
                } else {
                    openGallery()
                }
            }

            // MARK: - CAMERA BUTTON
            camButt.setOnClickListener {
                if (!mmp.checkPermissionForCamera()) {
                    mmp.requestPermissionForCamera()
                } else {
                    openCamera()
                }
            }
        }
    } // end onCreate()

    var outPutfileUri: Uri? = null

    // IMAGE HANDLING METHODS ------------------------------------------------------------------------
    var CAMERA = 0
    var GALLERY = 1

    // OPEN CAMERA
    private fun openCamera() {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, CAMERA);
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(
            Environment.getExternalStorageDirectory(),
            "MyPhoto.jpg"
        )
        outPutfileUri = Uri.fromFile(file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri)
        startActivityForResult(intent, CAMERA)
    }

    // OPEN GALLERY
    private fun openGallery() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY)
    }

    // IMAGE PICKED DELEGATE
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            var bm: Bitmap? = null

            // Camera
            if (requestCode == CAMERA) {
                val uri = outPutfileUri.toString()
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.contentResolver, outPutfileUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }


                // Gallery
            } else if (requestCode == GALLERY) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(
                        applicationContext.contentResolver,
                        data?.data
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            // Call method to pass image to Other Activity
            bm?.let {
                val scaledBitmap = scaleBitmapToMaxSize(800, bm)
                passBitmapToOtherActivity(scaledBitmap)
            } ?: Toast.makeText(this, "Couln't load image to continue", Toast.LENGTH_SHORT).show()
        }
    }

    //END  IMAGE HANDLING METHODS -------------------------------------------------------------------------------------
    private fun passBitmapToOtherActivity(bitmap: Bitmap): String? {

        // Save Bitmap into the Device (to pass it to the other Activity)
        var fileName: String? = "imagePassed"
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo = openFileOutput(fileName, MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            fo.close()

            // Go to CreateMeme
            val i = Intent(this@MainActivity, ImageEditor::class.java)
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
            fileName = null
        }
        return fileName
    }

    companion object {
        fun scaleBitmapToMaxSize(
            maxSize: Int,
            bm: Bitmap
        ): Bitmap {
            val outWidth: Int
            val outHeight: Int
            val inWidth = bm.width
            val inHeight = bm.height
            if (inWidth > inHeight) {
                outWidth = maxSize
                outHeight = inHeight * maxSize / inWidth
            } else {
                outHeight = maxSize
                outWidth = inWidth * maxSize / inHeight
            }
            return Bitmap.createScaledBitmap(bm, outWidth, outHeight, false)
        }
    }
} //@end
