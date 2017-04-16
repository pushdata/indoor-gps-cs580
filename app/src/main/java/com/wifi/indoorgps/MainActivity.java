package com.wifi.indoorgps;

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

public class MainActivity extends MapActivity {

    public static final int SCAN_DELAY = 1000;
    public static final int SCAN_INTERVAL = 1000;
    public static final int MAX_SCAN_THREADS = 2;
    private static Handler handler = new Handler();
    private int threadCount = 0;
    private Runnable refreshArea = new Runnable() {
        public void run() {
        }
    };

    private boolean threadPaused = false;

    private HashMap<String, Integer> readings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readings = new HashMap<String, Integer>();

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (threadPaused == false) {
                    wifi.startScan();
                }
            }

        }, SCAN_DELAY, SCAN_INTERVAL);
    }

    public void onResume() {
        super.onResume();

        threadPaused = false;
    }

    public void onPause() {
        super.onPause();
        threadPaused = true;
    }

    @Override
    public void onReceiveWifiScanResults(final List<ScanResult> results) {
        FingerprintManager application = (FingerprintManager) getApplication();
        final ArrayList<Model> fingerprints = application.getFingerprintData(areaSelected);

        if (results.size() > 0 && fingerprints.size() > 0 && threadCount <= MAX_SCAN_THREADS) {
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
                        Integer value = measurements.get(key);
                        Integer oldValue = readings.get(key);
                        if (oldValue == null) {
                            readings.put(key, value);
                        } else if (value == null) {
                            readings.remove(key);
                        } else {
                            value = (int) (oldValue * 0.4f + value * 0.6f);
                            readings.put(key, value);
                        }
                    }


                    Model f = new Model(readings);

                    Model closestMatch = f.getClosestMatch(fingerprints);

                    handler.post(refreshArea);

                    threadCount--;
                }
            };
            t.start();
        }
    }

    public void startTrainActivity() {
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
                Toast.makeText(getApplicationContext(), "Traning Activity Called", Toast.LENGTH_LONG).show();
                startTrainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
