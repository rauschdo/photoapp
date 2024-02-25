package de.rauschdo.photoapp

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.rauschdo.photoapp.LegacyFilterImpl.brightnessEffect
import de.rauschdo.photoapp.LegacyFilterImpl.contrastEffect
import de.rauschdo.photoapp.LegacyFilterImpl.grayscaleEffect
import de.rauschdo.photoapp.LegacyFilterImpl.invertEffect
import de.rauschdo.photoapp.LegacyFilterImpl.sepiaEffect
import de.rauschdo.photoapp.databinding.ActivityImageEditorBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ImageEditor : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityImageEditorBinding

    var originalBm: Bitmap? = null
    var adjBm: Bitmap? = null
    var progDialog: ProgressDialog? = null
    var imageData: ByteArray? = null

    // Array of frame images
    var framesList = intArrayOf(
        R.drawable.frame0, R.drawable.frame1,
        R.drawable.frame2, R.drawable.frame3,
        R.drawable.frame4, R.drawable.frame5,
        R.drawable.frame6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lock to Portrait orientation
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        ActivityImageEditorBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(binding.root)
        }

        // Set back Button
        // supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Photoapp Editor"

        with(binding) {
            //Image from MainActivity
            val bitmap: Bitmap?
            try {
                bitmap = BitmapFactory.decodeStream(openFileInput("imagePassed"))
                finalImage.setImageBitmap(bitmap)

                // Set original bitmap
                originalBm = bitmap
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            // Filters Button
            filtersButt.setOnClickListener {
                filtersView.visibility = View.VISIBLE
                filtersButt.setBackgroundResource(R.drawable.filters_on)
                adjustView.visibility = View.INVISIBLE
                adjustButt.setBackgroundResource(R.drawable.adjust)
                framesView.visibility = View.INVISIBLE
                framesButt.setBackgroundResource(R.drawable.frames)
                captionButt.setBackgroundResource(R.drawable.caption)
                captionEditText.visibility = View.INVISIBLE
            }


            // Adjustment Button
            adjustButt.setOnClickListener {
                adjBm = (finalImage.drawable as BitmapDrawable).bitmap
                adjustView.visibility = View.VISIBLE
                adjustButt.setBackgroundResource(R.drawable.adjust_on)
                filtersView.visibility = View.INVISIBLE
                filtersButt.setBackgroundResource(R.drawable.filters)
                framesView.visibility = View.INVISIBLE
                framesButt.setBackgroundResource(R.drawable.frames)
                captionButt.setBackgroundResource(R.drawable.caption)
                captionEditText.visibility = View.INVISIBLE
            }

            // Frames Button
            framesButt.setOnClickListener {
                framesView.visibility = View.VISIBLE
                framesButt.setBackgroundResource(R.drawable.frames_on)
                adjustView.visibility = View.INVISIBLE
                adjustButt.setBackgroundResource(R.drawable.adjust)
                filtersView.visibility = View.INVISIBLE
                filtersButt.setBackgroundResource(R.drawable.filters)
                captionButt.setBackgroundResource(R.drawable.caption)
                captionEditText.visibility = View.INVISIBLE
            }


            // Caption button
            captionButt.setOnClickListener {
                captionButt.setBackgroundResource(R.drawable.caption_on)
                captionEditText.visibility = View.VISIBLE
                captionEditText.isFocusable = true

                // Tap Enter on keyboard
                captionEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        /// Hide captionEditTxt
                        captionTxt.text = captionEditText.text.toString()
                        captionEditText.visibility = View.INVISIBLE
                        // Dismiss keyboard
                        val `in` = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        `in`.hideSoftInputFromWindow(captionEditText.windowToken, 0)
                        return@OnKeyListener true
                    }
                    false
                })
                framesView.visibility = View.INVISIBLE
                framesButt.setBackgroundResource(R.drawable.frames)
                adjustView.visibility = View.INVISIBLE
                adjustButt.setBackgroundResource(R.drawable.adjust)
                filtersView.visibility = View.INVISIBLE
                filtersButt.setBackgroundResource(R.drawable.filters)
            }


            // MARK: - BRIGHTNESS SLIDER ------------------------------------------------------------
            brightnessSeek.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    adjBm?.let {
                        val intensity = seekBar.progress + -50
                        brightnessEffect(finalImage, it, intensity)
                    }
                }
            })


            // MARK: - GENERATE FRAME BUTTONS INTO SCROLLVIEW --------------------------------
            for (i in framesList.indices) {
                val layout = framesLayout

                // Setup the Buttons
                val btnTag = Button(this@ImageEditor)
                val height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    50f,
                    resources.displayMetrics
                ).toInt()
                val width = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    50f,
                    resources.displayMetrics
                ).toInt()
                val layoutParams = LinearLayout.LayoutParams(width, height)
                layoutParams.setMargins(5, 0, 0, 0)
                btnTag.layoutParams = layoutParams
                btnTag.id = i
                btnTag.setBackgroundResource(framesList[i])
                btnTag.setOnClickListener(this@ImageEditor)

                //add button to the layout
                layout.addView(btnTag)
            }


            // MARK: - PHOTO FILTER BUTTONS --------------------------------------------

            // Original
            originalButt.setOnClickListener { finalImage.setImageBitmap(originalBm) }

            // Instant
            instantButt.setOnClickListener {
                originalBm?.let { itBitmap ->
                    val red = 111.0
                    val green = 78.0
                    val blue = 55.0
                    val depth = 300
                    sepiaEffect(
                        finalImage,
                        itBitmap,
                        depth,
                        red / 255.0,
                        green / 255.0,
                        blue / 255.0
                    )
                    val processedBm = (finalImage.drawable as BitmapDrawable).bitmap
                    val intensity = 30
                    brightnessEffect(finalImage, processedBm, intensity)
                }
            }


            // Invert
            invertButt.setOnClickListener {
                originalBm?.let { itBitmap ->
                    invertEffect(finalImage, itBitmap)
                }
            }

            // Tonal
            tonalButt.setOnClickListener {
                originalBm?.let { itBitmap ->
                    val red = 100.0
                    val green = 100.0
                    val blue = 100.0
                    grayscaleEffect(finalImage, itBitmap, red / 255.0, green / 255.0, blue / 255.0)
                }
            }

            // Noir
            noirButt.setOnClickListener {
                originalBm?.let { itBitmap ->
                    val intensity = 51.0
                    contrastEffect(finalImage, itBitmap, intensity)
                }
            }


            // Vintage
            vintageButt.setOnClickListener {
                originalBm?.let { itBitmap ->
                    val red = 178.0
                    val green = 76.0
                    val blue = 2.0
                    val depth = 150
                    sepiaEffect(
                        finalImage,
                        itBitmap,
                        depth,
                        red / 255.0,
                        green / 255.0,
                        blue / 255.0
                    )
                }
            }

            // Vintage 2
            vintageButt2.setOnClickListener {
                originalBm?.let { itBitmap ->
                    val red = 164.0
                    val green = 178.0
                    val blue = 2.0
                    val depth = 150
                    sepiaEffect(
                        finalImage,
                        itBitmap,
                        depth,
                        red / 255.0,
                        green / 255.0,
                        blue / 255.0
                    )
                }
            }


            // Light Blue
            lightBlueButt.setOnClickListener {
                originalBm?.let { itBitmap ->
                    val red = 92.0
                    val green = 199.0
                    val blue = 255.0
                    val intensity = 180
                    sepiaEffect(
                        finalImage,
                        itBitmap,
                        intensity,
                        red / 255.0,
                        green / 255.0,
                        blue / 255.0
                    )
                }
            }


            // Light Green
            lightGreenButt.setOnClickListener {
                originalBm?.let { itBitmap ->
                    val red = 92.0
                    val green = 255.0
                    val blue = 120.0
                    val intensity = 180
                    sepiaEffect(
                        finalImage,
                        itBitmap,
                        intensity,
                        red / 255.0,
                        green / 255.0,
                        blue / 255.0
                    )
                }
            }


            // Light Red
            lightRedButt.setOnClickListener {
                originalBm?.let { itBitmap ->
                    val red = 255.0
                    val green = 108.0
                    val blue = 108.0
                    val intensity = 180
                    sepiaEffect(
                        finalImage,
                        itBitmap,
                        intensity,
                        red / 255.0,
                        green / 255.0,
                        blue / 255.0
                    )
                }
            }
        }
    } // end onCreate()

    private fun createImageAndSave() {
        val v: View = binding.cropView
        v.isDrawingCacheEnabled = true
        val bmp = v.drawingCache?.let { Bitmap.createBitmap(it) }
        v.isDrawingCacheEnabled = false
        val sdCard = Environment.getExternalStorageDirectory()
        val dir = File(sdCard.absolutePath + "/photoapp")
        dir.mkdirs()
        val fileName = String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(dir, fileName)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(outFile)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            Toast.makeText(this@ImageEditor, "Image saved under:\n$outFile", Toast.LENGTH_LONG)
                .show()
            fos.flush()
            fos.close()
            Log.d(
                "TAG",
                "onPictureTaken - wrote bytes: " + bmp?.byteCount + " to " + outFile.absolutePath
            )
            //Toast.makeText(ImageEditor.this, "Image Saved", Toast.LENGTH_LONG).show();
            refreshGallery(outFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // MENU ON ACTION BAR
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_image_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.save -> {
                //Dialog or save directly?

                // Init a ProgressDialog
                progDialog = ProgressDialog(this)
                progDialog?.setTitle(R.string.app_name)
                progDialog?.setMessage("Preparing image for saving...")
                progDialog?.isIndeterminate = false
                progDialog?.setIcon(R.drawable.bob)
                progDialog?.show()
                binding.cropView.postDelayed({
                    createImageAndSave()
                    progDialog?.dismiss()
                }, 2000)

                //new SaveImageTask().execute(imageData);
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    // OH HI MARK: SET FRAME IMAGE ------------------------------------------------
    override fun onClick(v: View) {
        binding.frameImg.setImageResource(framesList[v.id])
    }

    // OH HI MARK: SAVE EDITED IMAGE LOCALLY ------------------------------------------------
    private fun refreshGallery(file: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.setData(Uri.fromFile(file))
        sendBroadcast(mediaScanIntent)
    } /*
    //Daten rein -> verarbeitung im Stream -> erzeugen einer File im System und raus
    private class SaveImageTask extends AsyncTask<byte[], Void, Void>
    {
        @Override
        protected Void doInBackground(byte[]... data)
        {
            FileOutputStream outStream = null;

            // Write to SD Card
            try
            {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/photoapp");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d("TAG", "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());
                Toast.makeText(ImageEditor.this, "Image Saved", Toast.LENGTH_LONG).show();
                refreshGallery(outFile);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally { }
            return null;
        }
    }
    */
} //@end
