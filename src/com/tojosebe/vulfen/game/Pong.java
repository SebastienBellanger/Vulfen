package com.tojosebe.vulfen.game;

import com.vulfox.math.Vector2f;

public class Pong {

	public enum Type {
		COW, PENGUIN
	};

	public Type PongType;
	public Vector2f position;
	public Vector2f velocity;

	private int mImageResource;
	private float mWidth;
	private float mHeight;
	private float mMinSpeed = 500.0f;
	private float mMaxSpeed = 1700.0f;
	private float mFriction = 35.0f;
	private float mRadius;

	public Pong() {

	}
	/**
	 * @return the imageResource
	 */
	public int getImageResource() {
		return mImageResource;
	}

	/**
	 * @param imageResource
	 *            the imageResource to set
	 */
	public void setImageResource(int imageResource) {
		this.mImageResource = imageResource;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return mWidth;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(float width) {
		this.mWidth = width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return mHeight;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(float height) {
		this.mHeight = height;
	}

	/**
	 * @return the minSpeed
	 */
	public float getMinSpeed() {
		return mMinSpeed;
	}

	/**
	 * @param minSpeed
	 *            the minSpeed to set
	 */
	public void setMinSpeed(float minSpeed) {
		this.mMinSpeed = minSpeed;
	}

	/**
	 * @return the maxSpeed
	 */
	public float getMaxSpeed() {
		return mMaxSpeed;
	}

	/**
	 * @param maxSpeed
	 *            the maxSpeed to set
	 */
	public void setMaxSpeed(float maxSpeed) {
		this.mMaxSpeed = maxSpeed;
	}

	/**
	 * @return the friction
	 */
	public float getFriction() {
		return mFriction;
	}

	/**
	 * @param friction
	 *            the friction to set
	 */
	public void setFriction(float friction) {
		this.mFriction = friction;
	}

	/**
	 * @return the position
	 */
	public Vector2f getPosition() {
		return this.position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	/**
	 * @return the mRadius
	 */
	public float getRadius() {
		return mRadius;
	}

	/**
	 * @param mRadius
	 *            the mRadius to set
	 */
	public void setRadius(float mRadius) {
		this.mRadius = mRadius;
	}

}
