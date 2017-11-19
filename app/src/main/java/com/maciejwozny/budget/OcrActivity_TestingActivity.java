package com.maciejwozny.budget;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.maciejwozny.budget.ocr.BillReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OcrActivity_TestingActivity extends AppCompatActivity {
    private static final String TAG = OcrActivity_TestingActivity.class.getSimpleName();
    private static final int REQUEST_TAKE_PHOTO = 1;
    EditText brightnessEditText;
    EditText contrastEditText;
    Button applyButton;
    ImageView imgView;
    TextView ocr;
    Bitmap img;
    String tessDatapath = "";
    private String mCurrentPhotoPath = "/storage/emulated/0/Android/data/com.maciejwozny.budget/files/Pictures/JPEG_20171118_202622_-2114757523.jpg";
    private Button takePhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = "pol";
                tessDatapath = getFilesDir()+ "/tesseract/";
                checkFile(new File(tessDatapath + "tessdata/"));
                Log.d(TAG, "photo path: " + mCurrentPhotoPath);
                TessBaseAPI mTess = new TessBaseAPI();
                mTess.init(tessDatapath, lang);

                String OCRresult = null;
                mTess.setImage(img);
                OCRresult = mTess.getUTF8Text();
                Log.i(TAG, OCRresult);
                ocr.setText(OCRresult);
                Snackbar.make(getCurrentFocus(), OCRresult, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ocr.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.INVISIBLE);
            }
        });

        brightnessEditText = (EditText)findViewById(R.id.brightnessEditText);
        contrastEditText = (EditText)findViewById(R.id.contrastEditText);
        applyButton = (Button) findViewById(R.id.applyButton);
        takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
        imgView = (ImageView) findViewById(R.id.imageView);
        ocr = (TextView) findViewById(R.id.ocrTextView);

        applyButton.setOnClickListener(new ApplyListener());
        takePhotoButton.setOnClickListener(new TakePhotoListener());
    }

    class ApplyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            float brightness = Float.parseFloat(brightnessEditText.getText().toString());
            float contrast = Float.parseFloat(contrastEditText.getText().toString());

            img = loadImg(mCurrentPhotoPath);
            img = BillReader.changeBitmapContrastBrightness(img, contrast, brightness);

            ocr.setVisibility(View.INVISIBLE);
            imgView.setVisibility(View.VISIBLE);
            imgView.setImageBitmap(img);
        }
    }

    class TakePhotoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent();
        }
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    private void checkFile(File dir) {
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        if(dir.exists()) {
            String datafilepath = tessDatapath + "/tessdata/pol.traineddata";
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {
            String filepath = tessDatapath + "/tessdata/pol.traineddata";
            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("pol.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }


            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.maciejwozny.budget.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Log.d(TAG, "photo path: " + mCurrentPhotoPath);

            ocr.setVisibility(View.INVISIBLE);
            imgView.setVisibility(View.VISIBLE);
            imgView.setImageBitmap(loadImg(mCurrentPhotoPath));
        }
    }

    private Bitmap loadImg(String filename) {
        img = BitmapFactory.decodeFile(filename);
        try {
            ExifInterface exif = new ExifInterface(filename);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (orientation) {
                case 3:
                    img = rotateBitmap(img, 180);
                    break;
                case 6:
                    img = rotateBitmap(img, 90);
                    break;
                case 8:
                    img = rotateBitmap(img, -90);
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

}
