package com.example.makeupmymind;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.net.Uri;
import android.graphics.Bitmap;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.MediaStore;
import android.widget.Button;
import android.view.View;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cloudinary.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
public class CameraActivity extends AppCompatActivity {
    private SpeechRecognizer speech;
    private Intent recognizer_intent;
    private Button button;
    private String text;
    private boolean speechOver = false;
    static final String TAG = "MyActivity";

    HashSet<String> colors = new HashSet<>();

    private Button takePictureButton;
    private ImageView imageview;
//    private Uri file;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    //public static String pictureFilePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColors();
        setContentView(R.layout.activity_camera);
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
        takePictureButton = (Button) findViewById(R.id.takePhoto);
        imageview = (ImageView) findViewById(R.id.imageview);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    takePicture();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
            analyzeVoice(data.get(0).toString());
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }
    public void takePicture() throws IOException {
        File file = new File("/storage/emulated/0/Pictures/MyCameraApp/name.jpg");
        file.createNewFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri outputFileUri = Uri.fromFile(file);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, 1);
//        intent.setType("image/*");
//        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                File file = new File("/storage/emulated/0/Pictures/MyCameraApp/name.jpg");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byteArray = stream.toByteArray();

                try {
                    //convert array of bytes into file
                    FileOutputStream fileOutputStream =
                            new FileOutputStream("/storage/emulated/0/Pictures/MyCameraApp/name.jpg");
                    fileOutputStream.write(byteArray);
                    fileOutputStream.close();

                    System.out.println("Done");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageview.setImageBitmap(imageBitmap);
                //                File file = new File("/storage/emulated/0/Pictures/MyCameraApp/name.jpg");
                System.out.println("ResultCode=" +RESULT_OK);
//                imageview.setImageURI(file);
            }
        }
    }
    private static boolean createFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return false;
            }
        }

        File file;
        if(type == MEDIA_TYPE_IMAGE) { ;
            try {
                Log.d(TAG, mediaStorageDir.getPath());
                file = new File(mediaStorageDir.getPath() + File.separator + "name.jpg");
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
//
//    public  Bitmap getContactBitmapFromURI(Context context, Uri uri) {
//        try {
//
//            InputStream input = context.getContentResolver().openInputStream(uri);
//            if (input == null) {
//                return null;
//            }
//            return BitmapFactory.decodeStream(input);
//        }
//        catch (FileNotFoundException e)
//        {
//
//        }
//        return null;
//
//    }

//    public  File saveBitmapIntoSDCardImage(Context context, Bitmap finalBitmap) {
//
//        String root = Environment.getExternalStorageDirectory().toString();
//        myDir.mkdirs();
//
//        String fname = "file_name" + ".jpg";
//        File file = new File (myDir, fname);
//
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return file;
//    }

    public void analyzeVoice(String voice) {
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
        String lowerColor = color.toLowerCase();
        Global.setColor(lowerColor);
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
