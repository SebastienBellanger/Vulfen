package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;

import com.tojosebe.vulfen.component.SpriteComponent;
import com.vulfox.math.Vector2f;

public class Pong extends SpriteComponent {

	public enum Type {
		COW, PENGUIN
	};

	private int maxGroth = +2;
	private int maxShrink = +4;
	private int mShrinkSteps = 2;
	private int mTimesShrinken = 0;

	public Vector2f velocity;
	private Type mType;

	private int mCurrentGroth = 0;

	private float mMinSpeed = 500.0f;
	private float mMaxSpeed = 1700.0f;
	private float mFriction = 35.0f;
	private float mRadius;

	public Pong(Pong pong) {
		super(pong.getSpriteBitmap(), true, pong.isAnimateFromLarge(), pong
				.isAnimateFromSmall(), pong.getAnimationMillis(), pong.getPosition()
				.getX(), pong.getPosition().getY(), pong.getWidth(), pong
				.getHeight());
		setPosition(new Vector2f(pong.getPosition()));
		velocity = pong.velocity;
		mMinSpeed = pong.mMinSpeed;
		mMaxSpeed = pong.mMaxSpeed;
		mFriction = pong.mFriction;
		mRadius = pong.mRadius;
		mType = pong.mType;
	}

	public Pong(Bitmap bitmap, boolean antialias, boolean animateFromLarge,
			boolean animateFromSmall, int animationTimeMillis, float x,
			float y, float width, float height) {
		super(bitmap, antialias, animateFromLarge, animateFromSmall,
				animationTimeMillis, x, y, width, height);
		setPosition(new Vector2f(x, y));
		mRadius = width * 0.5f;
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

	/**
	 * @return the currentGroth
	 */
	public int getCurrentGroth() {
		return mCurrentGroth;
	}

	/**
	 * @param currentGroth
	 *            the currentGroth to set
	 */
	public void setCurrentGroth(int currentGroth) {
		this.mCurrentGroth = currentGroth;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return mType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.mType = type;
	}

	/**
	 * @return the maxGroth
	 */
	public int getMaxGroth() {
		return maxGroth;
	}

	/**
	 * @param maxGroth
	 *            the maxGroth to set
	 */
	public void setMaxGroth(int maxGroth) {
		this.maxGroth = maxGroth;
	}

	/**
	 * @return the maxShrink
	 */
	public int getMaxShrink() {
		return maxShrink;
	}

	/**
	 * @param maxShrink
	 *            the maxShrink to set
	 */
	public void setMaxShrink(int maxShrink) {
		this.maxShrink = maxShrink;
	}

	/**
	 * @return the shrinkSteps
	 */
	public int getShrinkSteps() {
		return mShrinkSteps;
	}

	/**
	 * @param shrinkSteps
	 *            the shrinkSteps to set
	 */
	public void setShrinkSteps(int shrinkSteps) {
		this.mShrinkSteps = shrinkSteps;
	}

	/**
	 * @return the timesShrinken
	 */
	public int getTimesShrinken() {
		return mTimesShrinken;
	}

	/**
	 * @param timesShrinken
	 *            the timesShrinken to set
	 */
	public void setTimesShrinken(int timesShrinken) {
		this.mTimesShrinken = timesShrinken;
	}

}
