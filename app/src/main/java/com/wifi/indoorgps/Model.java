package com.wifi.indoorgps;


import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

//Model Class

public class Model {
    PointF locationPoint;
    HashMap<String, Integer> readings;
    private String location_name = "";
    private int id;
    private String location;
    public Model() {
        id = 0;
        location = "";
    }
    public Model(HashMap<String, Integer> measurements) {
        this();
        readings = measurements;
    }

    public Model(HashMap<String, Integer> measurements, String map, String room_name) {
        this(measurements);
        location = map;
        location_name = room_name;
    }

    public Model(int id, String map, PointF location, String room_name) {
        this();
        locationPoint = location;
        location_name = room_name;

    }

    public Model(int id, String map, PointF location, HashMap<String, Integer> measurements, String room_name) {
        this(id, map, location, room_name);
        readings = measurements;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int inputId) {
        id = inputId;
    }

    public String getMap() {
        return location;
    }

    public void setMap(String map) {
        location = map;
    }

    public void setLocation(float x, float y) {
        locationPoint = new PointF(x, y);
    }

    public PointF getLocation() {
        return locationPoint;
    }

    public void setLocation(PointF location) {
        locationPoint = location;
    }

    public HashMap<String, Integer> getMeasurements() {
        return readings;
    }

    public void setMeasurements(HashMap<String, Integer> measurements) {
        readings = measurements;
    }

    //Compare the Fingerprint Provided
    public float compare(Model Model) {
        float result = 0f;

        HashMap<String, Integer> fingerprintMeasurements = Model.getMeasurements();
        TreeSet<String> keys = new TreeSet<String>();
        keys.addAll(readings.keySet());
        keys.addAll(fingerprintMeasurements.keySet());

        for (String key : keys) {
            int value = 0;
            Integer fValue = fingerprintMeasurements.get(key);
            Integer mValue = readings.get(key);
            value = (fValue == null) ? -119 : (int) fValue;
            value -= (mValue == null) ? -119 : (int) mValue;
            result += value * value;

        }
        return result;
    }

    //Get the closest Fingerprint Data
    public Model getClosestMatch(ArrayList<Model> fingerprints) {
        Model closest = null;
        float bestScore = -1;

        if (fingerprints != null) {
            for (Model Model : fingerprints) {
                float score = compare(Model);
                if (bestScore == -1 || bestScore > score) {
                    bestScore = score;
                    closest = Model;
                }
            }
        }
        return closest;
    }

}
