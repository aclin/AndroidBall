package com.alin.androidball;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;

public class AndroidBall extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        DisplayMetrics deviceMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(deviceMetrics);
        
        getAndroidBallView().setDpi(deviceMetrics.xdpi, deviceMetrics.ydpi);
        //getAndroidBallView().setGravity(0.0f, 9.8f);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    
    private AndroidBallView getAndroidBallView() {
    	return (AndroidBallView) findViewById(R.id.androidBall_view);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	sensorManager.unregisterListener(this, accelerometer);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
		switch (getWindowManager().getDefaultDisplay().getOrientation()) {
			case Surface.ROTATION_0:
				getAndroidBallView().setGravity(-event.values[0], event.values[1]);
				break;
			case Surface.ROTATION_90:
				getAndroidBallView().setGravity(event.values[1], event.values[0]);
				break;
			case Surface.ROTATION_180:
				getAndroidBallView().setGravity(event.values[0], -event.values[1]);
				break;
			case Surface.ROTATION_270:
				getAndroidBallView().setGravity(-event.values[1], -event.values[0]);
				break;
		}
	}
}