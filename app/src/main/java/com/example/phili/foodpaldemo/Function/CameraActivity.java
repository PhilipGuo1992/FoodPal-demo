package com.example.phili.foodpaldemo.Function;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.phili.foodpaldemo.R;

/**
 * Created by tanis on 2018-02-27.
 */

public class CameraActivity extends AppCompatActivity {

    private ImageView editPhoto;

    //Citation[18] "Android Developers", Developer.android.com, 2018. [Online]. Available: https://developer.android.com/index.html. [Accessed: 08- Apr- 2018].
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {

        editPhoto = findViewById(R.id.editphoto);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            editPhoto.setImageBitmap(imageBitmap);
        }
    }
}
