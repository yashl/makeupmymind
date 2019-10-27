package com.example.makeupmymind;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Key;

public class JSONParse {

    public static double calculation(JSONArray arr) {
        try {
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                if (obj.has("faceLandmarkss")) {
                    JSONObject leftPupil = obj.getJSONObject("pupilLeft");
                    JSONObject eyeLeftOuter = obj.getJSONObject("eyeLeftOuter");
                    double leftPupilX = leftPupil.getDouble("x");
                    double eyeLeftOuterX = eyeLeftOuter.getDouble("x");
                    return leftPupilX - eyeLeftOuterX;
                }
            }

            return 0;
        } catch (Exception e) {
            throw new Error("Unable to get json from file");
        }
    }

    public static String getFileName(String str) {
        String[] sp = str.split("/");
        return sp[sp.length-1];
    }
}
