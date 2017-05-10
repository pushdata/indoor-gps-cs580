package com.wifi.indoorgps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "result.db";
    public static final String FINGERPRINTS_TABLE = "fingerprints";
    public static final String READINGS_TABLE = "readings";
    public static final String FINGERPRINTS_TABLE_CREATE = "CREATE TABLE 'fingerprints' "
            + "('fid' INTEGER PRIMARY KEY,'location_name' TEXT NOT NULL,'x_co' FLOAT, 'y_co' FLOAT,'room_name' TEXT NOT NULL)";

    public static final String READINGS_TABLE_CREATE = "CREATE TABLE 'readings' "
            + "('rid' INTEGER PRIMARY KEY ,'fid' INTEGER ,'ssid' TEXT NOT NULL ,'rssi' INTEGER NOT NULL)";


    public ResultDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //Creating 2 Tables to store Fingerprints(Offline phase) and Query Phase Readings
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FINGERPRINTS_TABLE_CREATE);
        db.execSQL(READINGS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FINGERPRINTS_TABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + READINGS_TABLE_CREATE);
        onCreate(db);
    }

    //Delete Specific Fingerprint Data
    public void deleteFingerprint(Model fingerprint) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] values = new String[]{String.valueOf(fingerprint.getId())};
        db.delete(FINGERPRINTS_TABLE, "fid=?", values);

        values = new String[]{String.valueOf(fingerprint.getId())};
        db.delete(READINGS_TABLE, "fid=?", values);

        db.close();
    }

    //Delete All Fingerprints
    public void deleteAllFingerprints() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FINGERPRINTS_TABLE, null, null); // delete all fingerprints
        db.delete(READINGS_TABLE, null, null); // delete all measurements
        db.close();
    }

    //Add Specific Fingerprint Data
    public void addFingerprint(Model fingerprint) {
        SQLiteDatabase db = this.getWritableDatabase();
        PointF location = fingerprint.getLocation();

        ContentValues cv = new ContentValues();
        cv.put("location_name", fingerprint.getMap());
        cv.put("x_co", location.x);
        cv.put("y_co", location.y);
        cv.put("room_name", fingerprint.getLocation_name());
        Map<String, Integer> measurements;
        long fingerprintId = db.insert(FINGERPRINTS_TABLE, null, cv);

        if (fingerprintId != -1) {
            measurements = fingerprint.getMeasurements();
            for (String key : measurements.keySet()) {
                int value = measurements.get(key);
                ContentValues c = new ContentValues();
                c.put("fid", fingerprintId);
                c.put("ssid", key);
                c.put("rssi", value);
                db.insert(READINGS_TABLE, null, c);
            }
        }

        db.close();
    }

    //Get Specific Fingerprint Data By Providing Fingerprint ID
    public Model getFingerprint(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Model fingerprint = null;
        HashMap<String, Integer> measurements = null;
        String[] values = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(FINGERPRINTS_TABLE,
                new String[]{"fid", "location_name", "x_co", "y_co", "room_name"},
                "fid=?", values, null, null, null, null);

        if (cursor.moveToFirst()) {
            PointF point = new PointF(cursor.getFloat(2), cursor.getFloat(3));
            String location = cursor.getString(1);
            String room_name = cursor.getString(4);
            measurements = getMeasurements(id);
            fingerprint = new Model(id, location, point, measurements, room_name);
        }
        cursor.close();
        db.close();
        return fingerprint;
    }

    //Retrieve All Fingerprints from the Database
    public ArrayList<Model> getAllFingerprints() {
        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, Integer> measurements = null;
        Model fingerprint = null;
        ArrayList<Model> fingerprints = new ArrayList<Model>();
        Cursor cursor = db.rawQuery("select *from " + FINGERPRINTS_TABLE, null); // SQL query
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int id = cursor.getInt(0);
            String location = cursor.getString(1);
            PointF point = new PointF(cursor.getFloat(2), cursor.getFloat(3));
            String room_name = cursor.getString(4);
            measurements = getMeasurements(id);
            fingerprint = new Model(id, location, point, measurements, room_name);
            fingerprints.add(fingerprint);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return fingerprints;
    }

    //Get Readings from Database by providing fingerprint ID
    public HashMap<String, Integer> getMeasurements(int fingerprintId) {
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<String, Integer> measurements = new HashMap<String, Integer>();

        String[] values = new String[]{String.valueOf(fingerprintId)};

        Cursor cursor = db.query(READINGS_TABLE,
                new String[]{"ssid", "rssi"},
                "fid=?", values, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String ssid = cursor.getString(0);
            int rssi = cursor.getInt(1);
            measurements.put(ssid, rssi);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return measurements;
    }

    //Return Fingerprint Count from Offline Phase
    public int getFingerprintCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select *from " + FINGERPRINTS_TABLE, null); // SQL query
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}