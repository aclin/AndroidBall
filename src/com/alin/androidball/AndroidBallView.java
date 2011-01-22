package com.alin.androidball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidBallView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "AndroidBallView";
	private static final float NANOSECONDS_PER_SECOND = 1000000000.0f;
	private static final float GRAVITY_IN_METERS = 9.8f;
	private static final float GRAVITY_IN_INCHES = 32.2f * 12.0f;
	
	private androidBallLoop abLoop;
	private float xdpi;
	private float ydpi;
	
	public AndroidBallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		abLoop = new androidBallLoop();
		getHolder().addCallback(this);
	}
	
	private class androidBallLoop extends Thread {
		private long prevTime;
		private float dTime;
		private float gravX;
		private float gravY;
		
		Ball b = new Ball();
		
		private androidBallLoop() {
			
		}
		
		@Override
		public void run() {
			prevTime = System.nanoTime();
			updateTimer();
			while (!isInterrupted()) {
				synchronized (this) {
					updateTimer(); //keep track of the amount of time passed
					updatePhysics();
					updateAnimation();
					updateView();
				}
			}
		}
		
		private void updateView() {
			Canvas canvas = null;
			Paint mpaint = new Paint();
			
			try {
				canvas = getHolder().lockCanvas();
				canvas.clipRect(0, 0, getWidth(), getHeight());
				canvas.drawColor(Color.WHITE);
				synchronized (this) {
					mpaint.setStyle(Paint.Style.FILL_AND_STROKE);
					mpaint.setColor(Color.RED);
					canvas.drawCircle(b.x, b.y, b.r, mpaint);
				}
			} finally {
				getHolder().unlockCanvasAndPost(canvas);
			}
		}

		private void updateAnimation() {
			b.roll(dTime);
		}

		private void updatePhysics() {
			b.accelerate(dTime, gravX, gravY);
		}
		
		private void updateTimer() {
			long curTime = System.nanoTime();
			dTime = (curTime - prevTime) / NANOSECONDS_PER_SECOND;
			prevTime = curTime;
		}
		
		private void setGravity(float gx, float gy) {
			gravX = xdpi * (GRAVITY_IN_INCHES/200.0f) * (gx / GRAVITY_IN_METERS);
			gravY = ydpi * (GRAVITY_IN_INCHES/200.0f) * (gy / GRAVITY_IN_METERS);
		}
	}
	
	public void setGravity(float gx, float gy) {
		abLoop.setGravity(gx, gy);
	}
	
	public void setDpi(float xdpi, float ydpi) {
		this.xdpi = xdpi;
		this.ydpi = ydpi;
	}
	
	private class Ball {
		private float x, y;
		private float vx, vy;
		private float r;
		
		private Ball() {
			x = 130;
			y = 180;
			r = 10.0f;
			
			vx = 0.0f;
			vy = 0.0f;
		}
		
		private void roll(float dt) {
			x += vx * dt;
			y += vy * dt;
		}
		
		private void accelerate(float dt, float ax, float ay) {
			vx += ax * dt;
			vy += ay * dt;
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i(TAG, "Surface changed");
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "Start drawing loop");
		abLoop.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "Stop drawing loop");
		abLoop.interrupt();
	}

}
