package com.example.makeupmymind;

import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.*;

import java.util.HashMap;
import java.util.Map;

public class ImageProcessor
{

    static Cloudinary cloudinary = new Cloudinary();

    public static Thread method1()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("cloud_name", "yashl");

                    cloudinary.uploader().unsignedUpload("s3://bigdashchungus.s3.us-east-2.amazonaws.com/test.jpg", "hkupsid3", params);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        return thread;
    }
}
