package com.wifi.indoorgps;

import android.app.Application;

import java.util.ArrayList;

//Fingerprint Manager

public class FingerprintManager extends Application {
    ArrayList<Model> fingerprintsToRemove = null;
    private ArrayList<Model> fingerprints = null;
    private ResultDB db;

    @Override
    public void onCreate() {
        super.onCreate();
        fingerprints = new ArrayList<Model>();
        db = new ResultDB(this);
    }

    public void addFingerprint(Model model) {
        fingerprints.add(model);
        db.addFingerprint(model);
    }

    public ArrayList<Model> getFingerprintData(String map) {
        ArrayList<Model> fingerprintData = new ArrayList<Model>();
        for (Model Model : fingerprintData) {
            if (Model.getMap().compareTo(map) == 0) {
                fingerprints.add(Model);
            }
        }
        return fingerprints;

    }

    public ArrayList<Model> getFingerprintData() {
        return fingerprints;
    }

    public void getFingerprintsFromDatabase() {
        fingerprints = db.getAllFingerprints();
    }

    public void deleteAllFingerprints() {
        db.deleteAllFingerprints();
        fingerprints.clear();
    }

    public void deleteAllFingerprints(String map) {
        fingerprintsToRemove = new ArrayList<Model>();
        for (Model model : fingerprints) {
            if (model.getMap().compareTo(map) == 0) {
                fingerprintsToRemove.add(model);
            }
        }
        for (Model model : fingerprintsToRemove) {
            fingerprints.remove(model);
        }
    }


}
