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
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.List;


//MapActivity

public class MapActivity extends Activity implements OnTouchListener {
    private static final int MENU_ITEM_CHOOSE_FLOOR = 1;
    private static final int MENU_ITEM_BASEMENT = 2;
    protected WifiManager wifi;
    protected BroadcastReceiver receiver;
    protected FingerprintManager application;
    protected String areaSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (FingerprintManager) getApplication();
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void onStart() {
        super.onStart();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                onReceiveWifiScanResults(wifi.getScanResults());

            }
        };

        registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void onReceiveWifiScanResults(List<ScanResult> results) {

    }

    public boolean onTouch(View v, MotionEvent event) {
        v.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        SubMenu sub = menu.addSubMenu(Menu.NONE, MENU_ITEM_CHOOSE_FLOOR, Menu.NONE, "Choose Level");
        sub.add(Menu.NONE, MENU_ITEM_BASEMENT, Menu.NONE, "Basement");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_BASEMENT:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

