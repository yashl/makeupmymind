package com.example.makeupmymind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataAnalysis();
    }

    private void dataAnalysis() {
        BufferedReader br = null;
        BufferedReader ps = null;
        InputStream product_catolog = getResources().openRawResource(R.raw.product_catalog);
        InputStream sku_metadata = getResources().openRawResource(R.raw.sku_metadata);
        String line = "";
        String cvsSplitBy = ",";
        String pvsSplitBy = ("\\|");
        HashSet<String> prods = new HashSet<>();
        HashSet<String> prodId = new HashSet<>();
        HashSet<String> ultaLink = new HashSet<>();
        String eyes = "color_eyes";
        String colorEyes = "Black";
        String url = "https://www.ulta.com/ulta?productId=";

        try {

            br = new BufferedReader(
                    new InputStreamReader(sku_metadata, Charset.forName("UTF-8")));
            while ((line = br.readLine()) != null) {
                Log.d("MainActivity", line);
                String[] product = line.split(cvsSplitBy);
                if(eyes.equals(product[1]) && colorEyes.equals(product[2])) {
                    if(!prods.contains(product[0])) {
                        prods.add(product[0]);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            ps = new BufferedReader(
                    new InputStreamReader(product_catolog, Charset.forName("UTF-8")));
            while ((line = ps.readLine()) != null) {
                String[] productLog = line.split(pvsSplitBy);
                if(prods.contains(productLog[0]) && productLog[5].equals("Eyeshadow")) {
                    prodId.add(productLog[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    ps.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for(String s : prodId) {
            ultaLink.add(url + s);
        }
        for(String s : ultaLink) {
            Log.d("MainActivity", s);
        }
    }
}
