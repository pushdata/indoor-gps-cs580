package com.wifi.indoorgps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


public class MainActivity extends AppCompatActivity {
    public static String location = "";
    public static FileOutputStream outputStream;
    public static PrintWriter file;
    public static File out;
    public static WifiManager wifi;
    public static LocationManager mgr;
    static String output;
    TextView tv;
    private Button startScan;
    private Button stopScan;
    private Intent intent;
    private EditText textBox;
    private PendingIntent pendingService;
    private AlarmManager alarmManager;
    //  this.startScan = (Button) findViewById(R.id.button);
    //this.stopScan = (Button) findViewById(R.id.button1);
    private View.OnClickListener scanStartedListener = new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            MainActivity.this.tv.setText(R.string.scan);
            onStartService();
        }
    };
    private View.OnClickListener scanStoppedListener = new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            MainActivity.this.tv.setText(R.string.stopscan);

        }
    };

    private void restoreMe(Bundle paramBundle) {
    }

    private void sendMessageToService(int paramInt) {
    }

    public void addKeyListener() {
        this.textBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent) {
                if ((paramAnonymousKeyEvent.getAction() == 0) && (paramAnonymousInt == 66)) {
                    Toast.makeText(MainActivity.this, MainActivity.this.textBox.getText(), Toast.LENGTH_SHORT).show();
                    MainActivity.location = MainActivity.this.textBox.getText().toString();
                    return true;
                }
                return false;
            }
        });
    }

    public void onTimerTick() {
        output = "";
        wifi.startScan();
        Object localObject = wifi.getScanResults();
        output = output + ((List) localObject).size() + "\n";
        localObject = ((List) localObject).iterator();
        for (; ; ) {
            if (!((Iterator) localObject).hasNext()) {
                localObject = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                file.write(output + (String) localObject + " " + MainActivity.location + "\n");
                file.flush();
                return;
            }
            ScanResult localScanResult = (ScanResult) ((Iterator) localObject).next();
            output = output + localScanResult.BSSID + " " + localScanResult.SSID + " " + localScanResult.level + "\n";
        }
    }

    public void onStartService() {
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.getConnectionInfo();
        wifi.getConfiguredNetworks();
        file = null;
        try {
            out = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fingerprint.txt");
            if (!out.exists()) {
                out.createNewFile();
            }
            outputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fingerprint.txt", true);
            file = new PrintWriter(outputStream);
        } catch (FileNotFoundException localFileNotFoundException) {
            for (; ; ) {
                Log.d("HHH", "no file!");
            }
        } catch (IOException localIOException) {
            for (; ; ) {
                Log.d("HHH", "something else");
            }
        }
        onTimerTick();
    }

    public void onBackPressed() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.startScan = ((Button) findViewById(R.id.button));
        this.stopScan = ((Button) findViewById(R.id.button1));
        this.tv = ((TextView) findViewById(R.id.textView2));
        this.textBox = ((EditText) findViewById(R.id.editText1));
        this.startScan.setOnClickListener(this.scanStartedListener);
        this.stopScan.setOnClickListener(this.scanStoppedListener);
        addKeyListener();
        restoreMe(savedInstanceState);
    }
}