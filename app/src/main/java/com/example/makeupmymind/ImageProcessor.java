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
                    String url1 = cloudinary.url().transformation(new Transformation()
                            .width(200).crop("thumb").gravity("face"))
                            .imageTag("name.jpg");
                    url1 = url1.substring(10,url1.length()-15);
                    System.out.println(url1);
                    Map<String, String> params = new HashMap<>();
                    params.put("cloud_name", "yashl");
                    params.put("api_key", "569563497487199");
                    params.put("api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg");
                    params.put("public_id", "face");
                    cloudinary.uploader().destroy("face", params);
                    params.put("detection", "adv_face");
                    Map map = cloudinary.uploader().upload(url1, params);

                    //create json
                    System.out.println(map);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return thread;


    }

    public static Thread getLeftEyeShadow(final int radius, final int height) {
        Map<String, String> params = new HashMap<>();
        params.put("cloud_name", "yashl");

        //create circle

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url1 = cloudinary.url().transformation(new Transformation()
                            .width(radius).height(radius).color(Global.getColor()).radius("max").effect("colorize"))
                            .imageTag("one_pixel");
                    Map<String, String> params = new HashMap<>();
                    params.put("cloud_name", "yashl");
                    params.put("api_key", "569563497487199");
                    params.put("api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg");
                    params.put("public_id", "circle_shadow");
                    cloudinary.uploader().destroy("circle_shadow", params);
                    cloudinary.uploader().upload(url1, params);

                    // implement json here


                    // crop circle
                    String url2 = cloudinary.url().transformation(new Transformation()
                            .gravity("north").crop("crop").height(height).width("iw").chain().effect("gradient_fade:40,y_0.8"))
                            .imageTag("circle_shadow.png");
                    params.put("public_id", "final_circle_shadow");
                    cloudinary.uploader().upload(url2, params);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return thread;

        //upload url1
//        try {
//            params.put("api_key", "569563497487199");
//            params.put("api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg");
//            params.put("public_id", "circle_shadow");
//            cloudinary.uploader().destroy("circle_shadow", params);
//            cloudinary.uploader().upload(url1, params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


}
