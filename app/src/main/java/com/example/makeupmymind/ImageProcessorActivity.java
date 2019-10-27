package com.example.makeupmymind;

import android.os.Bundle;
import org.json.JSONObject;
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

        try {
            String text = new String(Files.readAllBytes(Paths.get("advancedface.JSON")), StandardCharsets.UTF_8);
            JSONObject jsonObject =  new JSONObject(text);
            double diff = JSONParse.calculation(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}