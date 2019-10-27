package com.example.makeupmymind;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ImageProcessor {
    static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "yashl",
            "api_key", "569563497487199",
            "api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg"));

    public static Thread uploadImage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> params = new HashMap<>();
                    params.put("cloud_name", "yashl");
                    params.put("api_key", "569563497487199");
                    params.put("api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg");
                    params.put("public_id", "name");
                    Map result = cloudinary.uploader().destroy("name", params);
                    cloudinary.uploader().upload("https://bigdashchungus.s3.us-east-2.amazonaws.com/name.jpg", params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return thread;
    }

    public static Thread cropFace() {
        //crops image to find the face
        String url1 = cloudinary.url().transformation(new Transformation()
                .width(200).crop("thumb").gravity("face"))
                .imageTag("name");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url1 = "https://res.cloudinary.com/yashl/image/upload/c_thumb,w_200,g_face/name.jpg";
                    Map<String, String> params = new HashMap<>();
                    params.put("cloud_name", "yashl");
                    params.put("api_key", "569563497487199");
                    params.put("api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg");
                    params.put("public_id", "face");
                    cloudinary.uploader().destroy("face", params);
                    params.put("detection", "adv_face");
                    Map map = cloudinary.uploader().upload(url1, params);
                    System.out.println(map);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return thread;
        //create JSON


    }

    public static void getLeftEyeShadow() {
        Map<String, String> params = new HashMap<>();
        params.put("cloud_name", "yashl");

        //create circle
        int radius = 20;
        String color = "red";

        String url1 = cloudinary.url().transformation(new Transformation()
                .width(radius).height(radius).color(color).radius("max").effect("colorize"))
                .imageTag("one_pixel.");

        System.out.println(url1);

        //upload url1
        try {
            params.put("api_key", "569563497487199");
            params.put("api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg");
            params.put("public_id", "circle_shadow");
            cloudinary.uploader().destroy("circle_shadow", params);
            cloudinary.uploader().upload(url1, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //crop circle
        //String url2 = JSONParse.getFileName(url1);

    }

}
