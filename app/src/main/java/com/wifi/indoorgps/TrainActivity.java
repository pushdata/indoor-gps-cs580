package com.wifi.indoorgps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class TrainActivity extends GraphActivity {
    private static final int MIN_SCAN_COUNT = 3; // minimum amount of scans
    private static final int SCAN_COUNT = 3; // No. of Scans performed for calculating average of RSSI values
    private static final int SCAN_INTERVAL = 500; // Scan Interval in ms
    private String location_name = "";
    private int mScansLeft = 0;

    //Pointer on the Screen where the fingerprint will be stored
    private WifiCapture mPointer;

    private long mTouchStarted; // used for detecting tap events

    private ProgressDialog mLoadingDialog; // loading bar which is shown while scanning access points

    private HashMap<String, Integer> readings; // for storing measurement data during the scan

    private boolean mShowFingerprints = true;


    /** INSTANCE METHODS */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String floor = intent.getStringExtra(MainActivity.TRACK_FLOOR);
        setMap(Integer.valueOf(floor));
        setTitle(getTitle() + " Training Mode ");
    }

    @Override
    public void onReceiveWifiScanResults(List<ScanResult> results) {
        if (mScansLeft != 0 && mPointer != null) {
            //Store results only when there are more than 3 APs
            if (results.size() >= MIN_SCAN_COUNT) {
                mScansLeft--;

                //Store RSSI values to Hashmap
                HashMap<String, Integer> measurements = new HashMap<String, Integer>();
                for (ScanResult result : results) {
                    measurements.put(result.BSSID, result.level);
                }

                TreeSet<String> keys = new TreeSet<String>();
                keys.addAll(measurements.keySet());
                keys.addAll(readings.keySet());

                //Go through RSSI Values and calculate measurement for each Value
                for (String key : keys) {
                    Integer value = measurements.get(key);
                    Integer oldValue = readings.get(key);
                    // calculate new value for each RSSI Value
                    if(oldValue == null) {
                        readings.put(key, value + (-119 * (SCAN_COUNT - 1 - mScansLeft)));
                    } else if(value == null) {
                        readings.put(key, -119 + oldValue);
                    } else {
                        readings.put(key, value + oldValue);
                    }
                }

                //Keep Scanning
                if (mScansLeft > 0) {
                    scanNext();
                } else {
                    //Calculate Average Readings of User Fingerprints(Online Phase) by dividing RSSI by 3
                    for (String key : readings.keySet()) {
                        int value = (int) readings.get(key) / SCAN_COUNT;
                        readings.put(key, value);
                    }

                    Model f = new Model(readings, areaSelected, location_name);
                    f.setLocation(mPointer.getLocation());
                    mMap.createNewWifiPointOnMap(f, mShowFingerprints);
                    mApplication.addFingerprint(f);
                    mLoadingDialog.dismiss();
                }
            } else {
                //Not Enough APs found
                mLoadingDialog.dismiss(); // hide loading bar
                Toast.makeText(getApplicationContext(), "Failed to Create Fingerprint because of insufficient access points (Found "
                        + results.size() + ", Require at least " + MIN_SCAN_COUNT + ").", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        v.onTouchEvent(event); // handle map etc touch events

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTouchStarted = event.getEventTime(); // calculate tap start
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter Location Name");

// Set up the input
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        location_name = input.getText().toString();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case MotionEvent.ACTION_UP:
                //User Tap Detected
                if (event.getEventTime() - mTouchStarted < 150) {
                    // Get Touch Location and extract Co-Ordinates
                    PointF location = new PointF(event.getX(), event.getY());
                    // Add Bitmap pointer on the map and start wifi scan
                    if(mPointer == null) {
                        mPointer = mMap.createNewWifiPointOnMap(location);
                        mPointer.setLocation_name(location_name);
                        mPointer.activate();
                    } else {
                        mMap.setWifiPointViewPosition(mPointer, location);
                    }
                    refreshMap(); // Redraws map
                }
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // add menu items
        menu.add(Menu.NONE, 31, Menu.NONE, "Scan");
        menu.add(Menu.NONE, 32, Menu.NONE, "Delete all fingerprints");
        menu.add(Menu.NONE, 33, Menu.NONE, "Exit Training mode");
        super.onCreateOptionsMenu(menu); // items for changing map
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 31:
                if (mPointer == null && location_name != null) {
                    Toast.makeText(getApplicationContext(), "Please Enter location by placing the pointer on screen", Toast.LENGTH_LONG).show();
                } else {
                    startScan();
                }
                return true;
            case 32:
                deleteAllFingerprints();
                return true;
            case 33:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Start Scan and Show Scanning Dialog
    public void startScan() {
        mScansLeft = SCAN_COUNT;
        readings = new HashMap<String, Integer>();
        mLoadingDialog = ProgressDialog.show(this, "", "Scanning...", true); // show loading bar
        wifi.startScan();
    }

    //Create Timer to Schedule Scan at specified Scan Intervals
    public void scanNext() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                wifi.startScan();
            }

        }, SCAN_INTERVAL);
    }

    //Delete All Fingerprints from the Screen and Also From the Database
    public void deleteAllFingerprints() {
        mMap.deleteFingerprints(); // delete fingerprints from the screen
        mApplication.deleteAllFingerprints(); // delete fingerprints from the database
    }

    //Set Map Background By Providing Resource ID
    @Override
    public void setMap(int resId) {
        super.setMap(resId);
        mMap.deleteFingerprints(); // clear screen from fingerprints

        ArrayList<Model> fingerprints = mApplication.getFingerprintData(areaSelected); // load fingerprints from the database

        // add WifiPointViews on map with fingerprint data loaded from the database
        for(Model fingerprint : fingerprints) {
            mMap.createNewWifiPointOnMap(fingerprint, mShowFingerprints);
        }

    }
}
