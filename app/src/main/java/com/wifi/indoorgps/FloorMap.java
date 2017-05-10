package com.wifi.indoorgps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * Created by samara on 5/7/2017.
 */

public class FloorMap extends ImageView {

    private static final int MAP_TOUCHED = 1;

    //Matrices for capturing X Y coordinates
    private Matrix mMatrix = new Matrix();
    private Matrix mSavedMatrix = new Matrix();

    //Current Touch State
    private int mode = 0;

    private PointF mStart = new PointF();

    private ArrayList<WifiCapture> mWifiPoints;


    /** CONSTRUCTORS */

    public FloorMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWifiPoints = new ArrayList<WifiCapture>();
    }


    /** INSTANCE METHODS */

    //Place the pointer on the Map at specified values
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float[] values = new float[9];
        mMatrix.getValues(values);

        for(WifiCapture point : mWifiPoints) {
            point.drawWithTransformations(canvas, values);
        }
    }

    //When user touches the map this method will get invoked
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                onTouchStart(event);
                break;
            case MotionEvent.ACTION_UP:
                onTouchEnd();
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
        }

        return true;
    }

    //Get X,Y Co-Ordinates of the pointer on the map
    public void onTouchStart(MotionEvent event) {
        mSavedMatrix.set(mMatrix);
        mStart.set(event.getX(), event.getY());
        mode = MAP_TOUCHED;
    }

    //Triggered upon removing finger from the Map
    public void onTouchEnd() {
        mode = MAP_TOUCHED;
    }

    public void onTouchMove(MotionEvent event) {
            mapMove(event);

    }

    //Set matrix with translated X,Y coordinates
    public void mapMove(MotionEvent event) {
        mMatrix.set(mSavedMatrix);
        mMatrix.postTranslate(event.getX() - mStart.x, event.getY() - mStart.y);
        setImageMatrix(mMatrix);
    }


    //Return WifiCapture object with filled details
    public WifiCapture createNewWifiPointOnMap(PointF location) {
        WifiCapture wpView = new WifiCapture(getContext());
        float[] values = new float[9];
        mMatrix.getValues(values);
        location.set((location.x - values[2]) / values[0], (location.y - values[5]) / values[4]);
        wpView.setLocation(location);
        mWifiPoints.add(wpView);
        return wpView;
    }

    //Bind to fingerprint
    public WifiCapture createNewWifiPointOnMap(Model fingerprint) {
        WifiCapture wpView = new WifiCapture(getContext());
        wpView.setFingerprint(fingerprint);
        mWifiPoints.add(wpView);
        return wpView;
    }

    //Place the pointer on the MAP and set it to visible
    public WifiCapture createNewWifiPointOnMap(Model fingerprint, boolean visible) {
        WifiCapture wpView = createNewWifiPointOnMap(fingerprint);
        wpView.setVisible(visible);
        return wpView;
    }

    //Place a pointer on the map at specified location point
    public void setWifiPointViewPosition(WifiCapture pointer, PointF location) {
        float[] values = new float[9];
        mMatrix.getValues(values);
        location.set((location.x - values[2]) / values[0], (location.y - values[5]) / values[4]);
        pointer.setLocation(location);
    }

    //Return WifiCapture Objects
    public ArrayList<WifiCapture> getWifiPoints() {
        return mWifiPoints;
    }

    //Set WifiCapture Objects
    public void setWifiPoints(ArrayList<WifiCapture> wifiPoints) {
        mWifiPoints = wifiPoints;
    }

    //Set Wifi Points Visibility
    public void setWifiPointsVisibility(boolean visible) {
        for(WifiCapture point : mWifiPoints) {
            point.setVisible(visible);
        }
    }

    //Delete selected WifiCapture objects
    public void deleteFingerprints() {
        ArrayList<WifiCapture> itemsToRemove = new ArrayList<WifiCapture>();
        for (WifiCapture point : mWifiPoints) {
            if(point.getFingerprint() != null) {
                itemsToRemove.add(point);
            }
        }
        mWifiPoints.removeAll(itemsToRemove);
        invalidate();
    }
}
