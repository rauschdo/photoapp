package de.rauschdo.photoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageEditor extends AppCompatActivity implements View.OnClickListener {

    /* Views */
    ImageView imgView;
    ImageView frameImg;
    Bitmap originalBm;
    RelativeLayout cropView;
    ProgressDialog progDialog;
    TextView captionTxt;
    EditText captionEditText;
    Bitmap adjBm;
    byte[] imageData;


    // Array of frame images
    int[] framesList = new int[]{
            R.drawable.frame0, R.drawable.frame1,
            R.drawable.frame2, R.drawable.frame3,
            R.drawable.frame4,R.drawable.frame5,
            R.drawable.frame6,
    };






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Lock to Portrait orientation
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_image_editor);



        // Set back Button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Photoapp Editor");

        // Init Views
        imgView = (ImageView)findViewById(R.id.finalImage);
        frameImg = (ImageView)findViewById(R.id.frameImg);
        cropView = (RelativeLayout)findViewById(R.id.cropView);
        captionTxt = (TextView) findViewById(R.id.captionTxt);
        captionEditText = (EditText)findViewById(R.id.captionEditText);

        final HorizontalScrollView filtersView = (HorizontalScrollView)findViewById(R.id.filtersView);
        final RelativeLayout adjustView = (RelativeLayout)findViewById(R.id.adjustView);
        final HorizontalScrollView framesView = (HorizontalScrollView)findViewById(R.id.framesView);

        final Button filtersButt = (Button)findViewById(R.id.filtersButt);
        final Button adjButt = (Button)findViewById(R.id.adjustButt);
        final Button framesButt = (Button)findViewById(R.id.framesButt);
        final Button captionButt = (Button)findViewById(R.id.captionButt);




        //Image from MainActivity
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(ImageEditor.this.openFileInput("imagePassed"));
            imgView.setImageBitmap(bitmap);

            // Set original bitmap
            originalBm = bitmap;
        } catch (FileNotFoundException e) { e.printStackTrace(); }






        // MARK: - TOOLBAR BUTTONS ------------------------------------------------

        // Filters Button
        filtersButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersView.setVisibility(View.VISIBLE);
                filtersButt.setBackgroundResource(R.drawable.filters_on);

                adjustView.setVisibility(View.INVISIBLE);
                adjButt.setBackgroundResource(R.drawable.adjust);
                framesView.setVisibility(View.INVISIBLE);
                framesButt.setBackgroundResource(R.drawable.frames);
                captionButt.setBackgroundResource(R.drawable.caption);
                captionEditText.setVisibility(View.INVISIBLE);
            }
        });


        // Adjustment Button
        adjButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjBm = ((BitmapDrawable)imgView.getDrawable()).getBitmap();

                adjustView.setVisibility(View.VISIBLE);
                adjButt.setBackgroundResource(R.drawable.adjust_on);

                filtersView.setVisibility(View.INVISIBLE);
                filtersButt.setBackgroundResource(R.drawable.filters);
                framesView.setVisibility(View.INVISIBLE);
                framesButt.setBackgroundResource(R.drawable.frames);
                captionButt.setBackgroundResource(R.drawable.caption);
                captionEditText.setVisibility(View.INVISIBLE);
            }
        });

        // Frames Button
        framesButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                framesView.setVisibility(View.VISIBLE);
                framesButt.setBackgroundResource(R.drawable.frames_on);

                adjustView.setVisibility(View.INVISIBLE);
                adjButt.setBackgroundResource(R.drawable.adjust);
                filtersView.setVisibility(View.INVISIBLE);
                filtersButt.setBackgroundResource(R.drawable.filters);
                captionButt.setBackgroundResource(R.drawable.caption);
                captionEditText.setVisibility(View.INVISIBLE);
            }
        });


        // Caption button
        captionButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captionButt.setBackgroundResource(R.drawable.caption_on);
                captionEditText.setVisibility(View.VISIBLE);
                captionEditText.setFocusable(true);

                // Tap Enter on keyboard
                captionEditText.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            /// Hide captionEditTxt
                            captionTxt.setText(captionEditText.getText().toString());
                            captionEditText.setVisibility(View.INVISIBLE);
                            // Dismiss keyboard
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(captionEditText.getWindowToken(), 0);

                            return true;
                        }
                        return false;
                    }
                });

                framesView.setVisibility(View.INVISIBLE);
                framesButt.setBackgroundResource(R.drawable.frames);
                adjustView.setVisibility(View.INVISIBLE);
                adjButt.setBackgroundResource(R.drawable.adjust);
                filtersView.setVisibility(View.INVISIBLE);
                filtersButt.setBackgroundResource(R.drawable.filters);
            }
        });









        // MARK: - BRIGHTNESS SLIDER ------------------------------------------------------------
        final SeekBar brightnessSeek = (SeekBar)findViewById(R.id.brightnessSeek);
        brightnessSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int intensity = seekBar.getProgress() + -50;
                Filters.brightnessEffect(imgView, adjBm, intensity);
            }
        });





        // MARK: - GENERATE FRAME BUTTONS INTO SCROLLVIEW --------------------------------
        for (int i = 0; i<framesList.length; i++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.framesLayout);

            // Setup the Buttons
            Button btnTag = new Button(this);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            layoutParams.setMargins(5, 0, 0, 0);
            btnTag.setLayoutParams(layoutParams);
            btnTag.setId(i);
            btnTag.setBackgroundResource(framesList[i]);
            btnTag.setOnClickListener(this);

            //add button to the layout
            layout.addView(btnTag);
        }







        // MARK: - PHOTO FILTER BUTTONS --------------------------------------------

        // Original
        Button ob = (Button)findViewById(R.id.originalButt);
        ob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setImageBitmap(originalBm);
            }});


        // Instant
        Button ib = (Button)findViewById(R.id.instantButt);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double red = 111;
                double green = 78;
                double blue = 55;
                int depth = 300;
                Filters.sepiaEffect(imgView, originalBm, depth, red/255.0, green/255.0, blue/255.0);

                Bitmap processedBm = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
                int intensity = 30;
                Filters.brightnessEffect(imgView, processedBm, intensity);
            }});



        // Invert
        Button invB = (Button)findViewById(R.id.invertButt);
        invB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filters.invertEffect(imgView, originalBm);
            }});

        // Tonal
        Button tb = (Button)findViewById(R.id.tonalButt);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double red = 100;
                double green = 100;
                double blue = 100;
                Filters.grayscaleEffect(imgView, originalBm, red/255.0, green/255.0, blue/255.0);
            }});

        // Noir
        Button nb = (Button)findViewById(R.id.noirButt);
        nb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double intensity = 51;
                Filters.contrastEffect(imgView, originalBm, intensity);
            }});


        // Vintage
        Button vb = (Button)findViewById(R.id.vintageButt);
        vb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double red = 178;
                double green = 76;
                double blue = 2;
                int depth = 150;
                Filters.sepiaEffect(imgView, originalBm, depth, red/255.0, green/255.0, blue/255.0);
            }});

        // Vintage 2
        Button vb2 = (Button)findViewById(R.id.vintageButt2);
        vb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double red = 164;
                double green = 178;
                double blue = 2;
                int depth = 150;
                Filters.sepiaEffect(imgView, originalBm, depth, red/255.0, green/255.0, blue/255.0);
            }});


        // Light Blue
        Button fbB = (Button)findViewById(R.id.lightBlueButt);
        fbB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double red = 92;
                double green = 199;
                double blue = 255;
                int intensity = 180;
                Filters.sepiaEffect(imgView, originalBm, intensity, red/255.0, green/255.0, blue/255.0);
            }});


        // Light Green
        Button lgB = (Button)findViewById(R.id.lightGreenButt);
        lgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double red = 92;
                double green = 255;
                double blue = 120;
                int intensity = 180;
                Filters.sepiaEffect(imgView, originalBm, intensity, red/255.0, green/255.0, blue/255.0);
            }});


        // Light Red
        Button lrB = (Button)findViewById(R.id.lightRedButt);
        lrB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double red = 255;
                double green = 108;
                double blue = 108;
                int intensity = 180;
                Filters.sepiaEffect(imgView, originalBm, intensity, red/255.0, green/255.0, blue/255.0);
            }});



        // END PHOTO FILTER BUTTONS ------------------------------------------------





    }// end onCreate()








    public void createImageAndSave()
    {
        View v = cropView;
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/photoapp");
        dir.mkdirs();

        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);

        FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream(outFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(ImageEditor.this, "Image saved under:" + "\n" + outFile.toString(), Toast.LENGTH_LONG).show();
            fos.flush();
            fos.close();

            Log.d("TAG", "onPictureTaken - wrote bytes: " + bmp.getByteCount() + " to " + outFile.getAbsolutePath());
            //Toast.makeText(ImageEditor.this, "Image Saved", Toast.LENGTH_LONG).show();
            refreshGallery(outFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // MENU ON ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_editor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            // DEFAULT BACK BUTTON
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.save:
                //Dialog or save directly?

                // Init a ProgressDialog
                progDialog = new ProgressDialog(this);
                progDialog.setTitle(R.string.app_name);
                progDialog.setMessage("Preparing image for saving...");
                progDialog.setIndeterminate(false);
                progDialog.setIcon(R.drawable.bob);
                progDialog.show();

                cropView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createImageAndSave();
                        progDialog.dismiss();
                    }
                }, 2000);

                //new SaveImageTask().execute(imageData);


                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }



    // OH HI MARK: SET FRAME IMAGE ------------------------------------------------
    @Override
    public void onClick(View v)
    {
        frameImg.setImageResource(framesList[v.getId()]);
    }



    // OH HI MARK: SAVE EDITED IMAGE LOCALLY ------------------------------------------------
    private void refreshGallery(File file)
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    /*
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
}//@end
