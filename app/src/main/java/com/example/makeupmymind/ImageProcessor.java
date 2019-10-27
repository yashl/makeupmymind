package com.example.makeupmymind;

import com.cloudinary.Cloudinary;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageProcessor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processor);
        Cloudinary cloudinary = new Cloudinary();
//        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATVAIS4CHW4GJ42WI", "kAqGIHqI/fHAr+FTWIUzCWNDIYi9aSngtmnD7t/y");
//        AmazonS3Client s3Client = new AmazonS3Client(credentials);
//        AWSMobileClient.getInstance().initialize(this).execute();
//        String fileName = "test.jpg";
//        TransferUtility transferUtility =
//                TransferUtility.builder()
//                        .context(getApplicationContext())
//                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
//                        .s3Client(s3Client)
//                        .build();
//        File file = new File(fileName);
//        transferUtility.upload("bigdashchungus/" + fileName, file);
        Map<String, String> params = new HashMap<>();
        params.put("cloud_name", "yashl");

        try {
//            cloudinary.uploader().unsignedUpload("s3://bigdashchungus.s3.us-east-2.amazonaws.com/test.jpg", "hkupsid3", params);
            cloudinary.uploader().unsignedUpload("https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg", "hkupsid3", params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}