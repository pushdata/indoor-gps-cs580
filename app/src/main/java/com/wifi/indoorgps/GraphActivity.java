package com.wifi.indoorgps;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import java.util.List;


//MapActivity

public class GraphActivity extends Activity implements OnTouchListener {

    protected WifiManager wifi;
    protected FloorMap mMap; // map object
    protected BroadcastReceiver mReceiver; // for receiving wifi scan results
    protected FingerprintManager mApplication;
    protected String areaSelected; // id of the map which is currently being displayed


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMap = (FloorMap) findViewById(R.id.floorMap);
        mMap.setOnTouchListener(this);

        mApplication = (FingerprintManager) getApplication();
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.setMap(R.mipmap.floor); //Set Map Background
    }

    //Create Broadcast Receiver to receive scan results when they are available
    public void onStart() {
        super.onStart();

        mReceiver = new BroadcastReceiver ()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                onReceiveWifiScanResults(wifi.getScanResults());

            }
        };

        registerReceiver(mReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void onReceiveWifiScanResults(List<ScanResult> results) {

    }

    //Triggered upon Touching the Map
    public boolean onTouch(View v, MotionEvent event) {
        v.onTouchEvent(event);

        return true; // indicate event was handled
    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //Refresh the Map Screen to update the pointer location by redrawing the Map
    public void refreshMap() {
        mMap.invalidate();
    }

    //Set the Default Map and Scale it to Fit the Device Screen
    public void setMap(int resId) {
        areaSelected = String.valueOf(resId);
        mMap.setImageResource(resId); // change map image
        mMap.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}

