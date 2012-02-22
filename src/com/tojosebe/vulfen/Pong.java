package com.tojosebe.vulfen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class Pong {

	public static final float MASS_PER_VOLUME = 3.0f;

	private static final float MIN_RADIUS = 8.0f;
	private static final float MAX_RADIUS = 32.0f;

	private float mass;

	private float radius;

	private Vector2f position;

	private Vector2f velocity;

	private Vector2f force;

	private Paint mPaint;

	public Pong() {
		mPaint = new Paint();

		setRadius(16.0f);

		position = new Vector2f();
		velocity = new Vector2f();
		force = new Vector2f();
	}

	public void update(float timeStep) {
		Vector2f velocityDelta = Vector2fPool.getInstance().aquire();
		Vector2f positionDelta = Vector2fPool.getInstance().aquire();

		force.div(mass, velocityDelta);
		velocityDelta.mulT(timeStep);

		velocity.addT(velocityDelta);
		positionDelta.set(velocity);
		positionDelta.mulT(timeStep);

		position.addT(positionDelta);

		Vector2fPool.getInstance().release(velocityDelta);
		Vector2fPool.getInstance().release(positionDelta);
	}

	public void draw(Canvas canvas) {
		canvas.drawCircle(position.getX(), position.getY(), radius, mPaint);
	}

	public float getMass() {
		return mass;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		if (radius < MIN_RADIUS || radius > MAX_RADIUS)
			return;

		this.radius = radius;
		this.mass = radius * radius * (float) Math.PI * MASS_PER_VOLUME;
		int green = (int) (255 * (radius - MIN_RADIUS) / (MAX_RADIUS - MIN_RADIUS));
		mPaint.setColor(Color.rgb(255, green, 0));
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public Vector2f getForce() {
		return force;
	}
}
