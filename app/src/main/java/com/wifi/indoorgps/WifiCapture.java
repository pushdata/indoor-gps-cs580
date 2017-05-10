package com.wifi.indoorgps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

/**
 * Created by samar on 5/7/2017.
 */

public class WifiCapture extends View{

    private static final int INACTIVE_COLOR = Color.RED;
    private static final int ACTIVE_COLOR = Color.GREEN;
    float x, y;
    float width = 100.0f;
    float height = 100.0f;
    private Model mFingerprint;
    private String location_name;
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

    //Get location name
    public String getLocation_name() {
        return location_name;
    }

    //Set location name for Toast
    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
    }

    //Draw the BitMap pointer on the screen at specified X,Y Co-Ordinates
    protected void drawWithTransformations(Canvas canvas, float[] matrixValues) {
        RelativeXCoord = matrixValues[2] + floorMapLocation.x * matrixValues[0];
        RelativeYCoord = matrixValues[5] + floorMapLocation.y * matrixValues[4];

        if (floorMapVisible == true) {
            if (floorMapActive) {
                mPaint.setColor(ACTIVE_COLOR);
            } else {
                mPaint.setColor(INACTIVE_COLOR);
            }
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.human);
            bitmap = getResizedBitmap(bitmap, 150, 100);
            canvas.drawBitmap(bitmap, RelativeXCoord, RelativeYCoord, mPaint);
        }
    }

    //Get resized version of Bitmap icon which is used as pointer
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    //Get Co-ordinates of location
    public PointF getLocation() {
        return floorMapLocation;
    }

    //Set Location Co-ordinates of pointer
    public void setLocation(PointF location) {
        floorMapLocation = location;
    }

    //Get Fingerprint data
    public Model getFingerprint() {
        return mFingerprint;
    }

    //Set Fingerprint data of the location
    public void setFingerprint(Model fingerprint) {
        location_name = fingerprint.getLocation_name();
        mFingerprint = fingerprint;
        floorMapLocation = fingerprint.getLocation();
    }

    //Activate the pointer and set its visibility on screen
    public void activate() {
        floorMapActive = true;
    }

    public void deactivate() {
        floorMapActive = false;
    }

    //Set the pointer visibility on screen
    public void setVisible(boolean visible) {
        floorMapVisible = visible;
    }


}

