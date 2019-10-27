package com.example.makeupmymind;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.content.Intent;
import android.net.Uri;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.MediaStore;

import android.widget.Button;
import android.view.View;
import java.io.File;
import android.os.Environment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    private SpeechRecognizer speech;
    private Intent recognizer_intent;

    private Button button;
    private String text;
    private boolean speechOver = false;
    static final String TAG = "MyActivity";

    private Button takePictureButton;
    private ImageView imageview;
    private Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //voice recognition
        setContentView(R.layout.activity_camera);
        super.onCreate(savedInstanceState);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(new Listener());
        recognizer_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        button = (Button) findViewById(R.id.voiceRecognition);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.voiceRecognition)
                {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    speech.startListening(intent);
                }
            }
        });

        //taking pictures
        takePictureButton = (Button) findViewById(R.id.takePhoto);
        imageview = (ImageView) findViewById(R.id.imageview);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(imageview);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }
    public void stop(Context context) {
        Log.d(TAG, "stopped listening");
        speech.stopListening();
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
            for (int i = 0; i < data.size(); i++)
            {
                Log.d(TAG, "result " + data.get(i));

                str += data.get(i);
            }
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

    //determining whether or not we have the camera permission since the only reason we need the
    //external storage permission is to save images from the camera
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageview.setImageURI(file);
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
}


