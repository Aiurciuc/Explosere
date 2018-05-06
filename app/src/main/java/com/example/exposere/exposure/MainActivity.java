package com.example.exposere.exposure;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private LinearLayout layout;
    private ImageView image;

    private  Bitmap selectedBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
        image = findViewById(R.id.imageView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = findViewById(R.id.linearLayout);

        this.displayPhoto(R.drawable.poza1);
        this.displayPhoto(R.drawable.poza2);
        this.displayPhoto(R.drawable.poza3);

        FloatingActionButton addPicButton =  findViewById(R.id.addPic);
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
        }
        else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }


        addPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }

        });
    }

    private void displayPhoto(int photoName){
        ImageView imageView = new ImageView(this);
        imageView.setPadding(2, 2, 2, 2);
        final Bitmap imageSlideShow = BitmapFactory.decodeResource(
                getResources(), photoName);
        imageView.setImageBitmap(imageSlideShow);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        layout.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedBackImage = imageSlideShow;
                image.setImageBitmap(imageSlideShow);
            }
        });
    }

    //Load image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap colorImage = BitmapFactory.decodeFile(picturePath);
            Bitmap blackAndWhite = BlackAndWhite.convert(colorImage);

            Bitmap bleedingImage = BleedingEffect.convert(
                    scaleDown(selectedBackImage, Math.min(selectedBackImage.getHeight(),blackAndWhite.getHeight()), false),
                    scaleDown(blackAndWhite, Math.min(selectedBackImage.getHeight(),blackAndWhite.getHeight()), false));


            image.setImageBitmap(bleedingImage);
        }

    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = maxImageSize / realImage.getHeight();
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
