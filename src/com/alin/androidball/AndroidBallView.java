package com.alin.androidball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.alin.androidball.math.Physics;
import com.alin.androidball.math.Segment;
import com.alin.androidball.math.Vector2d;

public class AndroidBallView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "AndroidBallView";
	private static final float NANOSECONDS_PER_SECOND = 1000000000.0f;
	//private static final Segment NORTH_WALL = new Segment(60.0f, 60.0f, 250.0f, 60.0f);
	//private static final Segment SOUTH_WALL = new Segment(250.0f, 250.0f, 60.0f, 250.0f);
	//private static final Segment EAST_WALL = new Segment(250.0f, 60.0f, 250.0f, 250.0f);
	//private static final Segment WEST_WALL = new Segment(60.0f, 250.0f, 60.0f, 60.0f);
	private static final Segment NORTH_WALL = new Segment(0.0f, 0.0f, 259.0f, 0.0f);
	private static final Segment SOUTH_WALL = new Segment(259.0f, 369.0f, 0.0f, 369.0f);
	private static final Segment EAST_WALL = new Segment(259.0f, 0.0f, 259.0f, 369.0f);
	private static final Segment WEST_WALL = new Segment(0.0f, 369.0f, 0.0f, 0.0f);
	
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
		private Vector2d gravity = new Vector2d(0.0f, 9.8f);
		
		Ball b = new Ball();
		
		@Override
		public void run() {
			Log.i(TAG, "Start rolling the ball");
			prevTime = System.nanoTime();
			while (!isInterrupted()) {
				synchronized (this) {
					updateTimer(); //keep track of the amount of time passed
					updateState();
					updatePhysics();
					updateAnimation();
					updateView();
				}
			}
		}
		
		private void updateState() {
			
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
					canvas.drawCircle(b.pos.getX(), b.pos.getY(), b.r, mpaint);
					/*
					canvas.drawLine(NORTH_WALL.getStart().getX(), NORTH_WALL.getStart().getY(),
									NORTH_WALL.getEnd().getX(), NORTH_WALL.getEnd().getY(), mpaint);
					mpaint.setColor(Color.BLACK);
					canvas.drawLine(SOUTH_WALL.getStart().getX(), SOUTH_WALL.getStart().getY(),
									SOUTH_WALL.getEnd().getX(), SOUTH_WALL.getEnd().getY(), mpaint);
					mpaint.setColor(Color.BLUE);
					canvas.drawLine(EAST_WALL.getStart().getX(), EAST_WALL.getStart().getY(),
									EAST_WALL.getEnd().getX(), EAST_WALL.getEnd().getY(), mpaint);
					mpaint.setColor(Color.MAGENTA);
					canvas.drawLine(WEST_WALL.getStart().getX(), WEST_WALL.getStart().getY(),
									WEST_WALL.getEnd().getX(), WEST_WALL.getEnd().getY(), mpaint);
					*/
				}
			} finally {
				getHolder().unlockCanvasAndPost(canvas);
			}
		}

		private void updateAnimation() {
			b.roll(dTime);
		}

		private void updatePhysics() {
			b.accelerate(dTime, gravity);
			if (Physics.pointToLineDistance(b.pos, NORTH_WALL) <= b.r) {
				b.pos.set(b.pos.getX(), b.r);
				b.v.reflect(NORTH_WALL.getNormal());
				b.v.scale(b.bounce);
			}
			if (Physics.pointToLineDistance(b.pos, SOUTH_WALL) <= b.r) {
				b.pos.set(b.pos.getX(), SOUTH_WALL.getStart().getY() - b.r);
				b.v.reflect(SOUTH_WALL.getNormal());
				b.v.scale(b.bounce);
			}
			if (Physics.pointToLineDistance(b.pos, EAST_WALL) <= b.r) {
				b.pos.set(EAST_WALL.getStart().getX() - b.r, b.pos.getY());
				b.v.reflect(EAST_WALL.getNormal());
				b.v.scale(b.bounce);
			}
			if (Physics.pointToLineDistance(b.pos, WEST_WALL) <= b.r) {
				b.pos.set(b.r, b.pos.getY());
				b.v.reflect(WEST_WALL.getNormal());
				b.v.scale(b.bounce);
			}
		}
		
		private void updateTimer() {
			long curTime = System.nanoTime();
			dTime = (curTime - prevTime) / NANOSECONDS_PER_SECOND;
			prevTime = curTime;
		}
		
		private void setGravity(float gx, float gy) {
			gravity.set(xdpi * (Physics.GRAVITY_IN_INCHES/200.0f) * (gx / Physics.GRAVITY_IN_METERS),
						ydpi * (Physics.GRAVITY_IN_INCHES/200.0f) * (gy / Physics.GRAVITY_IN_METERS));
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
		private Vector2d pos, v;
		private float r;
		private float bounce = 0.8f;
		
		private Ball() {
			pos = new Vector2d(130.0f, 180.0f);
			r = 10.0f;
			
			v = new Vector2d(0.0f, 0.0f);
		}
		
		private void roll(float dt) {
			pos.multiplyAdd(dt, v);
		}
		
		private void accelerate(float dt, Vector2d acc) {
			v.multiplyAdd(dt, acc);
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
