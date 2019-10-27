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

    private int leftEyeRadius, leftEyeHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processor);

        Thread imageThread = ImageProcessor.uploadImage();
        imageThread.start();

        Thread cropThread = ImageProcessor.cropFace();
        cropThread.start();


        ImageProcessor.getLeftEyeShadow();
        //insert the file name you want to change here and get the "name.png"
        String picturefile = getFileName("../../../../name.png");

//       ImageProcessor.getLeftEyeShadow();
        parseMap();


    }

    public int getLeftEyeRadius(double leftPupilX, double eyeLeftOuterX) {
        return (int) (Math.round(leftPupilX - eyeLeftOuterX)*2.0);
    }

    public int getLeftEyeHeight(double eyeLeftOuterY,  double eyeLeftTopY) {
        return (int) Math.round(eyeLeftOuterY - eyeLeftTopY);

    }

    public String readFile() {
        String json = null;
        try {
            //calling the JSON file here
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

    public void parseMap() {
        int index = 0;
        String str = readFile(), temp = "";
        String[] sp = str.split(",");
        double leftPupilX = 0.0, eyeLeftOuterX = 0.0, eyeLeftOuterY = 0.0, eyeLeftTopY = 0.0;

        for(int i = 0; i < sp.length; i++) {
            if (sp[i].contains("left_pupil")) {
                index = sp[i].indexOf('x');
                temp = sp[i].substring(index + 2);
                leftPupilX = Double.parseDouble(temp);
            }

            if (sp[i].contains("left_outer")) {
                index = sp[i].indexOf('x');
                temp = sp[i].substring(index + 2);
                eyeLeftOuterX = Double.parseDouble(temp);
                index = sp[i+1].indexOf('y');
                temp = sp[i+1].substring(index+2, sp[i+1].indexOf("}"));
                eyeLeftOuterY = Double.parseDouble(temp);
            }

            if(sp[i].contains("left_top")) {
                index = sp[i+1].indexOf('y');
                temp = sp[i+1].substring(index+2, sp[i+1].indexOf("}"));
                eyeLeftTopY = Double.parseDouble(temp);
            }
        }

        leftEyeRadius = getLeftEyeRadius(leftPupilX, eyeLeftOuterX);
        leftEyeHeight = getLeftEyeHeight(eyeLeftOuterY, eyeLeftTopY);
    }

    public static String getFileName(String str) {
        String[] sp = str.split("/");
        return sp[sp.length - 1];
    }
}