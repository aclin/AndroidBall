package com.alin.androidball.math;

public class Physics {
	public static final float GRAVITY_IN_METERS = 9.8f;
	public static final float GRAVITY_IN_INCHES = 32.2f * 12.0f;
	
	public static float pointToLineDistance(Vector2d point, Segment segment) {
		Vector2d v = new Vector2d(segment.getStart());
		v.multiplyAdd(-1.0f, point);
		Vector2d u = new Vector2d(segment.asVector());
		
		float len2 = u.dot(u);
		float det = -1.0f * v.dot(u);
		
		if (det < 0 || det > len2) {
			u.multiplyAdd(-1.0f, point);
			return (float) Math.sqrt(Math.min(v.dot(v), u.dot(u)));
		}
		
		det = u.cross(v);
		return (float) Math.sqrt(det * det / len2);
	}
}
