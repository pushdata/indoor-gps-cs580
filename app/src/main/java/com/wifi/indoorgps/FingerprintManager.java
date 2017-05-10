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

    //Adding Fingerprint to database
    public void addFingerprint(Model model) {
        fingerprints.add(model);
        db.addFingerprint(model);
    }

    //Get Fingerprint data of selected map area
    public ArrayList<Model> getFingerprintData(String map) {
        ArrayList<Model> fingerprintData = new ArrayList<Model>();
        for (Model Model : fingerprintData) {
            if (Model.getMap().compareTo(map) == 0) {
                fingerprints.add(Model);
            }
        }
        return fingerprints;

    }

    //Get Fingerprint data without map
    public ArrayList<Model> getFingerprintData() {
        return fingerprints;
    }

    //Get All Fingerprints from Database
    public void getFingerprintsFromDatabase() {
        fingerprints = db.getAllFingerprints();
    }

    //Delete All Fingerprints from Database
    public void deleteAllFingerprints() {
        db.deleteAllFingerprints();
        fingerprints.clear();
    }

    //Delete All Fingerprints matching selected area
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
