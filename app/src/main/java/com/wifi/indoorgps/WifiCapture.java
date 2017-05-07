package com.wifi.indoorgps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

/**
 * Created by samar on 5/7/2017.
 */

public class WifiCapture extends View{

    private static final int INACTIVE_COLOR = Color.RED;
    private static final int ACTIVE_COLOR = Color.GREEN;

    private Model mFingerprint;

    private boolean floorMapActive;

    private Paint  mPaint; // draw color

    private PointF floorMapLocation; // location on screen
    private float mRadius; // circle radius

    // placeholders for calculated screen positions
    private float RelativeXCoord, RelativeYCoord;

    private boolean floorMapVisible;

    public WifiCapture(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(INACTIVE_COLOR);
        mPaint.setTextSize(25);
        mPaint.setAntiAlias(true);

        floorMapActive = false;
        floorMapVisible = true;
        mRadius = 10f;
        floorMapLocation = new PointF(0, 0);
        mFingerprint = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
    }

    protected void drawWithTransformations(Canvas canvas, float[] matrixValues) {
        RelativeXCoord = matrixValues[2] + floorMapLocation.x * matrixValues[0];
        RelativeYCoord = matrixValues[5] + floorMapLocation.y * matrixValues[4];

        if (floorMapVisible == true) { // draw only if set visible
            if (floorMapActive) { // choose draw color based on active state
                mPaint.setColor(ACTIVE_COLOR);
            } else {
                mPaint.setColor(INACTIVE_COLOR);
            }

            canvas.drawCircle(RelativeXCoord, RelativeYCoord, mRadius, mPaint);
        }
    }

    public PointF getLocation() {
        return floorMapLocation;
    }

    public void setLocation(PointF location) {
        floorMapLocation = location;
    }

    public void setSize(float radius) {
        mRadius = radius;
    }

    public Model getFingerprint() {
        return mFingerprint;
    }

    public void setFingerprint(Model fingerprint) {
        mFingerprint = fingerprint;
        floorMapLocation = fingerprint.getLocation();
    }

    public void activate() {
        floorMapActive = true;
    }

    public void deactivate() {
        floorMapActive = false;
    }

    public void setVisible(boolean visible) {
        floorMapVisible = visible;
    }


}

