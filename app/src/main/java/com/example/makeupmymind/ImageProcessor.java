package com.example.makeupmymind;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ImageProcessor {
    private static int leftEyeRadius, leftEyeHeight;
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
                    parseMap(map.toString());
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return thread;


    }

    public static Thread getLeftEyeShadow() {
        Map<String, String> params = new HashMap<>();
        params.put("cloud_name", "yashl");

        //create circle

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int radius = leftEyeRadius;
                    int height = leftEyeHeight;
                    String url1 = cloudinary.url().transformation(new Transformation()
                            .width(radius).height(radius).color(Global.getColor()).radius("max").effect("colorize"))
                            .imageTag("one_pixel.jpg");
                    url1 = url1.substring(10,url1.length()-2);
                    System.out.println(url1);
                    Map<String, String> params = new HashMap<>();
                    params.put("cloud_name", "yashl");
                    params.put("api_key", "569563497487199");
                    params.put("api_secret", "N48wEqzlwfWXXLct34JqtVVLSIg");
                    params.put("public_id", "circle_shadow");
                    cloudinary.uploader().destroy("circle_shadow", params);
                    System.out.println(url1);
                    cloudinary.uploader().upload(url1, params);

                    // implement json here


                    // crop circle
                    String url2 = cloudinary.url().transformation(new Transformation()
                            .gravity("north").crop("crop").height(height).width("iw").chain().effect("gradient_fade:40,y_0.8"))
                            .imageTag("circle_shadow.png");
                    params.put("public_id", "final_circle_shadow");
                    url2 = url2.substring(10);
                    System.out.println(url2);
                    cloudinary.uploader().destroy("final_circle_shadow", params);
                    cloudinary.uploader().upload(url2, params);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return thread;

    }

    public static void parseMap(String map) {
        int index = 0;
        String str = map, temp = "";
        String[] sp = str.split(",");
        double leftPupilX = 0.0, eyeLeftOuterX = 0.0, eyeLeftOuterY = 0.0, eyeLeftTopY = 0.0;

        for(int i = 0; i < sp.length; i++) {
            if (sp[i].contains("left_pupil")) {
                index = sp[i].indexOf('x');
                temp = sp[i].substring(index + 2);
                leftPupilX = Double.parseDouble(temp);
            }

            if (sp[i].contains("left_outer")) {
                index = sp[i].indexOf('x');
                temp = sp[i].substring(index + 2);
                eyeLeftOuterX = Double.parseDouble(temp);
                index = sp[i+1].indexOf('y');
                temp = sp[i+1].substring(index+2, sp[i+1].indexOf("}"));
                eyeLeftOuterY = Double.parseDouble(temp);
            }

            if(sp[i].contains("left_top")) {
                index = sp[i+1].indexOf('y');
                temp = sp[i+1].substring(index+2, sp[i+1].indexOf("}"));
                eyeLeftTopY = Double.parseDouble(temp);
            }
        }

        leftEyeRadius = getLeftEyeRadius(leftPupilX, eyeLeftOuterX);
        leftEyeHeight = getLeftEyeHeight(eyeLeftOuterY, eyeLeftTopY);
        System.out.println(leftEyeRadius);
        System.out.println(leftEyeHeight);
    }

    public static int getLeftEyeRadius(double leftPupilX, double eyeLeftOuterX) {
        return (int) (Math.round(leftPupilX - eyeLeftOuterX)*2.0);
    }

    public static int getLeftEyeHeight(double eyeLeftOuterY,  double eyeLeftTopY) {
        return (int) Math.round(eyeLeftOuterY - eyeLeftTopY);

    }


}
