package com.example.makeupmymind;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import androidx.appcompat.app.AppCompatActivity;

public class ImageProcessorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processor);

        Thread thread = ImageProcessor.uploadImage();
        thread.start();

        try {
            File file = new File("advancedface.JSON");
            JSONObject object = new JSONObject(file.toString());
            double diff = JSONParse.calculation(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}