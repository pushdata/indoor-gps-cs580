package com.wifi.indoorgps;

import android.content.Intent;
import android.graphics.PointF;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class MainActivity extends GraphActivity {
    Integer currentReading;
    Integer previousReading;
    private boolean trainMode = false;
    private HashMap<String, Integer> readings;
    Timer timer;
    TimerTask myTimerTask;
    public final static String TRACK_FLOOR = "com.inte.indoorpositiontracker.FLOOR";
    public static final int MAX_SCAN_THREADS = 2;
    private int threadCount = 0;
    private WifiCapture pointer;
    private static Handler handler = new Handler();

    private Runnable mRefreshMap = new Runnable() {
        public void run() {
            refreshMap();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readings = new HashMap<String, Integer>();

        pointer = mMap.createNewWifiPointOnMap(new PointF(-1000, -1000));
        pointer.activate();

        timer = new Timer();

        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (trainMode == false)
                    wifi.startScan();
            }
        };
        timer.schedule(myTimerTask, 1000, 1000);
    }

    public void onResume() {
        super.onResume();
        trainMode = false;
    }

    public void onPause() {
        super.onPause();

        trainMode = true;
    }

    @Override
    public void onReceiveWifiScanResults(final List<ScanResult> results) {
        FingerprintManager application = (FingerprintManager) getApplication();
        final ArrayList<Model> fingerprints = application.getFingerprintData(areaSelected);
        if(results.size() > 0 && fingerprints.size() > 0 && threadCount <= MAX_SCAN_THREADS) {
            Thread t = new Thread() {
                public void run() {
                    threadCount++;

                    HashMap<String, Integer> measurements = new HashMap<String, Integer>();
                    for (ScanResult result : results) {
                        measurements.put(result.BSSID, result.level);
                    }

                    TreeSet<String> keys = new TreeSet<String>();
                    keys.addAll(readings.keySet());
                    keys.addAll(measurements.keySet());

                    for (String key : keys) {
                         currentReading = measurements.get(key);
                         previousReading = readings.get(key);
                        if(previousReading == null) {
                            readings.put(key, currentReading);
                        } else if(currentReading == null) {
                            readings.remove(key);
                        } else {
                            currentReading = (int) (previousReading * 0.4f + currentReading * 0.6f);
                            readings.put(key, currentReading);
                        }
                    }

                    Model f = new Model(readings);

                    Model closestMatch = f.getClosestMatch(fingerprints);

                    pointer.setFingerprint(closestMatch);

                    handler.post(mRefreshMap);

                    threadCount--;
                }
            };
            t.start();
        }
    }

    public void startTrainActivity() {
        Intent intent = new Intent(MainActivity.this, TrainActivity.class);
        intent.putExtra(TRACK_FLOOR, areaSelected);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, 21, Menu.NONE, "Train Model");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 21:
                Toast.makeText(getApplicationContext(), "Training Activity Called", Toast.LENGTH_LONG).show();
                startTrainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}