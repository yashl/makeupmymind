package com.example.makeupmymind;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class ImageProcessor
{

    static Cloudinary cloudinary = new Cloudinary();

    public static Thread uploadImage()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("cloud_name", "yashl");
                    params.put("api_key", "569563497487199");
                    params.put("api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg");
                    params.put("public_id", "name.jpg");
                    Map result = cloudinary.uploader().destroy("name.jpg", params);
                    cloudinary.uploader().upload("https://bigdashchungus.s3.us-east-2.amazonaws.com/name.jpg", params);
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
