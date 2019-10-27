package com.example.makeupmymind;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Key;

public class JSONParse {
    public static double calculateLeftEyeShadow(JSONObject object) {
        try {
            JSONObject faceAttributes = object.getJSONObject("faceLandmarks");
            JSONObject leftPupil = faceAttributes.getJSONObject("pupilLeft");
            JSONObject eyeLeftOuter = faceAttributes.getJSONObject("eyeLeftOuter");

            double leftPupilX = leftPupil.getDouble("x");
            double eyeLeftOuterX = eyeLeftOuter.getDouble("x");

            return leftPupilX - eyeLeftOuterX;
        } catch (Exception e) {
            throw new Error("Unable to get json from file");
        }
    }
}
