package com.example.makeupmymind;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ImageProcessorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processor);

        Thread thread = ImageProcessor.uploadImage();
        thread.start();

        ImageProcessor.getLeftEyeShadow();
    }
}