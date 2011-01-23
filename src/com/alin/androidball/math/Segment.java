package com.alin.androidball.math;

public class Segment {
	private Vector2d start, end, vector, normal;
	private float length;
	
	public Segment(float sx, float sy, float ex, float ey) {
		start = new Vector2d(sx, sy);
		end = new Vector2d(ex, ey);
		vector = new Vector2d(ex - sx, ey - sy);
		normal = new Vector2d(vector.getY(), -vector.getX()).normalize();
	}
	
	public Segment(Vector2d start, Vector2d end) {
		this.start = start;
		this.end = end;
		vector = new Vector2d(end.getX() - start.getX(), end.getY() - start.getY());
		normal = new Vector2d(vector.getY(), -vector.getX()).normalize();
	}
	
	public Vector2d getStart() {
		return start;
	}
	
	public Vector2d getEnd() {
		return end;
	}
	
	public float getLength() {
		return length;
	}
	
	public Vector2d asVector() {
		return vector;
	}
	
	public Vector2d getNormal() {
		return normal;
	}
}
