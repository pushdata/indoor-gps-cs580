package com.wifi.indoorgps;


import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

//Model Class

public class Model {
    PointF locationPoint;
    float bestResult = -1;
    HashMap<String, Integer> readings;
    private int id;
    private int value = 0;
    private String location;

    public Model() {
        id = 0;
        location = "";
    }

    public Model(HashMap<String, Integer> measurements) {
        this();
        readings = measurements;
    }

    public Model(HashMap<String, Integer> measurements, String map) {
        this(measurements);
        location = map;
    }

    public Model(int id, String map, PointF location) {
        this();
        locationPoint = location;
    }

    public Model(int id, String map, PointF location, HashMap<String, Integer> measurements) {
        this(id, map, location);
        readings = measurements;
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

    public float compare(Model Model) {
        float result = 0f;

        HashMap<String, Integer> fingerprintMeasurements = Model.getMeasurements();
        TreeSet<String> keys = new TreeSet<String>();
        keys.addAll(readings.keySet());
        keys.addAll(fingerprintMeasurements.keySet());

        for (String key : keys) {
            int x = 0;
            value = 0;
            Integer fValue = fingerprintMeasurements.get(key);
            Integer mValue = readings.get(key);
            x = getValue(fValue, mValue);
            result += x * x;

        }
        return result;
    }

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

    public int getValue(Integer f, Integer m) {
        if (f == null) {
            value = -119;
        } else {
            value = (int) f;
        }
        if (f == null) {
            value = -119;
        } else {
            value = value - (int) m;
        }
        return value;
    }



}
