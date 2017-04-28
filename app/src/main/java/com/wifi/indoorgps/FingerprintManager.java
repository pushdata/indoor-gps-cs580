package com.wifi.indoorgps;

import android.app.Application;

import java.util.ArrayList;

//Fingerprint Manager

public class FingerprintManager extends Application {
    private ArrayList<Model> fingerprints;


    @Override
    public void onCreate() {
        super.onCreate();
        fingerprints = new ArrayList<Model>();
    }


    public void addFingerprint(Model Model) {
        fingerprints.add(Model);
    }

    public void deleteAllFingerprints() {
        fingerprints.clear();
    }

    public void deleteAllFingerprints(String map) {
        ArrayList<Model> itemsToRemove = new ArrayList<Model>();
        for (Model Model : fingerprints) {
            if (Model.getMap().compareTo(map) == 0) {
                itemsToRemove.add(Model);
            }
        }

        for (Model Model : itemsToRemove) {
            fingerprints.remove(Model); // delete from arraylist
        }
    }

    public ArrayList<Model> getFingerprintData(String map) {
        ArrayList<Model> fingerprints = new ArrayList<Model>();
        for (Model Model : fingerprints) {
            if (Model.getMap().compareTo(map) == 0) {
                fingerprints.add(Model);
            }
        }

        return fingerprints;
    }

    public ArrayList<Model> getFingerprintData() {

        return fingerprints;
    }
}
