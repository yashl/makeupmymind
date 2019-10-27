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

        //parse JSON file
        try {
            String str = readJSON();
            JSONArray jArray = new JSONArray(str);
            JSONObject jObj = jArray.getJSONObject(0);
            if (jObj.has("faceLandmarks")){
                JSONObject faceLandmarks = jObj.getJSONObject("faceLandmarks");
                //eye calculations
                leftEyeRadius = (int) Math.round(getLeftEyeRadius(faceLandmarks));
                leftEyeHeight = (int) Math.round(getLeftEyeHeight(faceLandmarks));
                Log.d("leftEyeRadius", leftEyeRadius + "");
                Log.d("leftEyeHeight", leftEyeHeight + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getLeftEyeRadius(JSONObject faceLandmarks) {
        try {
            JSONObject leftPupil = faceLandmarks.getJSONObject("pupilLeft");
            JSONObject eyeLeftOuter = faceLandmarks.getJSONObject("eyeLeftOuter");
            double leftPupilX = leftPupil.getDouble("x");
            double eyeLeftOuterX = eyeLeftOuter.getDouble("x");
            return (leftPupilX - eyeLeftOuterX)*2.0;
        } catch (JSONException e) {
             throw new Error("Unable to create JSON Object");
        }
    }

    public double getLeftEyeHeight(JSONObject faceLandmarks) {
        try {
            JSONObject eyeLeftTop = faceLandmarks.getJSONObject("eyeLeftTop");
            JSONObject eyeLeftOuter = faceLandmarks.getJSONObject("eyeLeftOuter");
            double eyeLeftTopy = eyeLeftTop.getDouble("y");
            double eyeLeftOuterY = eyeLeftOuter.getDouble("y");
            return eyeLeftOuterY - eyeLeftTopy;
        } catch (JSONException e) {
            throw new Error("Unable to create JSON Object");
        }
    }

    public String readJSON() {
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

    public static String getFileName(String str) {
        String[] sp = str.split("/");
        return sp[sp.length - 1];
    }
}