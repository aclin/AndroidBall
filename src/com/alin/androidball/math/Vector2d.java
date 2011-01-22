package com.alin.androidball.math;

public class Vector2d {
	private float x, y;
	
	public Vector2d(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(Vector2d v) {
		x = v.getX();
		y = v.getY();
	}
	
	public float magnitude() {
		return (float) Math.sqrt(dot(this));
	}
	
	public Vector2d add(Vector2d v) {
		x += v.getX();
		y += v.getY();
		return this;
	}
	
	public Vector2d multiply(Vector2d v) {
		x *= v.getX();
		y *= v.getY();
		return this;
	}
	
	public Vector2d scale(float s) {
		x *= s;
		y *= s;
		return this;
	}
	
	public Vector2d multiplyadd(float s, Vector2d v) {
		x += s * v.getX();
		y += s * v.getY();
		return this;
	}
	
	public float dot(Vector2d v) {
		return x * v.x + y * v.y;
	}
	
	public Vector2d normalize() {
		scale(1.0f / magnitude());
		return this;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
