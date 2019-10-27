package com.example.makeupmymind;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class VoiceRecognition extends AppCompatActivity {
    SpeechRecognizer sr;
    Button speak;
    String text;
    String speechStuff;
    boolean speechOver = false;
    static final String TAG = "MyActivity";
    HashSet<String> colors = new HashSet<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColors();
        speak = (Button) findViewById(R.id.voiceRecognition);
        speak.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.voiceRecognition)
                {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplication().getPackageName());

                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
                    sr.startListening(intent);
                }
            }
        });
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new Listener());
    }


    class Listener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
        }
        @Override
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
        }
        @Override
        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }
        @Override
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }
        @Override
        public void onEndOfSpeech()
        {
            speechOver = true;

            Log.d(TAG, "onEndofSpeech");
        }
        @Override
        public void onError(int error)
        {
            Log.d(TAG,  "error " +  error);
            text = "error: " + error;
        }
        @Override
        public void onResults(Bundle results)
        {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            speechStuff=data.get(0).toString();
            for (int i = 0; i < data.size(); i++)
            {
                Log.d(TAG, "result " + data.get(i).toString());
                str += data.get(i);
            }
            Log.d(TAG, str);
            analyzeVoice(str);

            text = "results: "+String.valueOf(data.size());
        }
        @Override
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }
        @Override
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }

    public void analyzeVoice(String voice) {
        Log.d(TAG, "************");
        for(String text : voice.split(" ")) {
            String capText = text.substring(0, 1).toUpperCase() + text.substring(1);
            Log.d(TAG, capText);
            if(colors.contains(capText)) {
                dataAnalysis(capText);
            }
        }
    }

    public void dataAnalysis(String color) {
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
        String colorEyes = color;
        String url = "https://www.ulta.com/ulta?productId=";

        try {

            br = new BufferedReader(
                    new InputStreamReader(sku_metadata, Charset.forName("UTF-8")));
            while ((line = br.readLine()) != null) {
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
            Log.d("StartActivity", s);
        }
    }

    public void setColors() {
        colors.add("Black");
        colors.add("Blue");
        colors.add("Red");
        colors.add("White");
    }

}
