package com.alin.androidball;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class AndroidBall extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        DisplayMetrics deviceMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(deviceMetrics);
        
        getAndroidBallView().setDpi(deviceMetrics.xdpi, deviceMetrics.ydpi);
        getAndroidBallView().setGravity(0.0f, 9.8f);
    }
    
    private AndroidBallView getAndroidBallView() {
    	return (AndroidBallView) findViewById(R.id.androidBall_view);
    }
}