package com.example.makeupmymind;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
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

        Thread imageThread = ImageProcessor.uploadImage(null);
        imageThread.start();

        Thread cropThread = ImageProcessor.cropFace();
        cropThread.start();


        ImageProcessor.getLeftEyeShadow();
        //insert the file name you want to change here and get the "name.png"
        String picturefile = getFileName("../../../../name.png");

//       ImageProcessor.getLeftEyeShadow();
        //parseMap();
    }

    public int getLeftEyeRadius(double leftPupilX, double eyeLeftOuterX) {
        return (int) (Math.round(leftPupilX - eyeLeftOuterX)*2.0);
    }
        Thread leftThread = ImageProcessor.getLeftEyeShadow();
        //leftThread.start();
        // String picturefile = getFileName("../../../../name.png");


    public static String getFileName(String str) {
        String[] sp = str.split("/");
        return sp[sp.length - 1];
    }
}