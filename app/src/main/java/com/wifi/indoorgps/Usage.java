package com.wifi.indoorgps;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by The Beast on 5/9/2017.
 */

public class Usage extends AppCompatActivity {
    Vibrator v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void kitchen(View view) {
        long[] pattern = new long[]{0, 400, 1000};
        v.vibrate(pattern, -1);
    }

    public void hall(View view) {
        long[] pattern = new long[]{0, 1000, 1000};
        v.vibrate(pattern, -1);
    }

    public void bedroom(View view) {
        long[] pattern = new long[]{0, 100, 1000};
        v.vibrate(pattern, -1);
    }
}