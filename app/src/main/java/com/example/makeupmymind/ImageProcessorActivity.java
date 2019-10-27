package com.example.makeupmymind;

import android.os.Bundle;

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

        Thread thread = ImageProcessor.uploadImage();
        thread.start();

        //insert the file name you want to change here
        String picturefile = JSONParse.getFileName("../../../../name.png");

//        ImageProcessor.getLeftEyeShadow();

        try {
            JSONArray jArray = new JSONArray(readJSONFromAsset("advancedface.JSON"));
            double diff = JSONParse.calculation(jArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String readJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = getAssets().open(filename);
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