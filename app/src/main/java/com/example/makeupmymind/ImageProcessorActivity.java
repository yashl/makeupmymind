package com.example.makeupmymind;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import androidx.appcompat.app.AppCompatActivity;

public class ImageProcessorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processor);

        Thread imageThread = ImageProcessor.uploadImage();
        imageThread.start();

        Thread cropThread = ImageProcessor.cropFace();
        cropThread.start();

        //insert the file name you want to change here
         String picturefile = JSONParse.getFileName("../../../../name.png");

        ImageProcessor.getLeftEyeShadow();

        try {
            String str = readJSONFromAsset();
            JSONArray jArray = new JSONArray(str);
            double diff = JSONParse.calculateLeftEyeRadius(jArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.advancedface);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}