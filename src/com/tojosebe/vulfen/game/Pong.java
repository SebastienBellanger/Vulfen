package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.tojosebe.vulfen.component.SpriteComponent;
import com.vulfox.math.Vector2f;

public class Pong {
	
	public enum Type {
		COW, PENGUIN
	};
	
	private int maxGroth = +2;
	private int maxShrink = +4;
	private int mShrinkSteps = 2;
	private int mTimesShrinken = 0;
	
	public Vector2f position;
	public Vector2f velocity;
	private Type mType;
	
	private int mCurrentGroth = 0;

	private int mImageResource;
	private float mWidth;
	private float mHeight;
	private float mMinSpeed = 500.0f;
	private float mMaxSpeed = 1700.0f;
	private float mFriction = 35.0f;
	private float mRadius;
	
	private Bitmap mBitmap;
	
	private SpriteComponent spriteComponent;
	
	private Rect mRect = new Rect();

	public Pong(Pong pong) {
		position = pong.position;
		velocity = pong.velocity;
		mImageResource = pong.mImageResource;
		mWidth = pong.mWidth;
		mHeight = pong.mHeight;
		mMinSpeed = pong.mMinSpeed;
		mMaxSpeed = pong.mMaxSpeed;
		mFriction = pong.mFriction;
		mRadius = pong.mRadius;
		createSprite();
	}

	private void createSprite() {
		spriteComponent = new SpriteComponent(mRect, mBitmap, false, false, true, 200);
	}

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

	/**
	 * @return the currentGroth
	 */
	public int getCurrentGroth() {
		return mCurrentGroth;
	}

	/**
	 * @param currentGroth the currentGroth to set
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
	 * @param type the type to set
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
	 * @param maxGroth the maxGroth to set
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
	 * @param maxShrink the maxShrink to set
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
	 * @param shrinkSteps the shrinkSteps to set
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
	 * @param timesShrinken the timesShrinken to set
	 */
	public void setTimesShrinken(int timesShrinken) {
		this.mTimesShrinken = timesShrinken;
	}

	/**
	 * @return the spriteComponent
	 */
	public SpriteComponent getSpriteComponent() {
		return spriteComponent;
	}

	public void setPosition(float x, float y) {
		this.position.setX(x);
		this.position.setY(y);
	}

}
