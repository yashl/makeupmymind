package com.example.makeupmymind;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Key;

public class JSONParse {

    public static double calculateLeftEyeRadius(JSONArray arr) {
        try {
            JSONObject obj = arr.getJSONObject(0);
            if (obj.has("faceLandmarks")) {
                JSONObject faceLandmarks = arr.getJSONObject(0).getJSONObject("faceLandmarks");
                JSONObject leftPupil = faceLandmarks.getJSONObject("pupilLeft");
                JSONObject eyeLeftOuter = faceLandmarks.getJSONObject("eyeLeftOuter");
                double leftPupilX = leftPupil.getDouble("x");
                double eyeLeftOuterX = eyeLeftOuter.getDouble("x");
                return (leftPupilX - eyeLeftOuterX)*2.0;
            }


            return 0;
        } catch (Exception e) {
            throw new Error("Unable to get json from file");
        }
    }

    public static String getFileName(String str) {
        String[] sp = str.split("/");
        return sp[sp.length - 1];
    }
}
