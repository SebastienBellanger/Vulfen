package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;

import com.vulfox.math.Vector2f;

public class BonusItem {

	public enum BonusItemType {
		EXTRA_PONG, SHRINKER, GROWER, SPLITTER, EXPLODER, TIMES2, TIMES3
	};

	private BonusItemType mItemType;

	private Vector2f mPosition;
	private Vector2f mVelocity = new Vector2f(0, 100.f);

	private Bitmap mBitmap;

	private float mWidth;
	private float mHeight;
	private float mRadius;

	/**
	 * @return the itemType
	 */
	public BonusItemType getItemType() {
		return mItemType;
	}

	/**
	 * @param itemType
	 *            the itemType to set
	 */
	public void setItemType(BonusItemType itemType) {
		this.mItemType = itemType;
	}

	/**
	 * @return the position
	 */
	public Vector2f getPosition() {
		return mPosition;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Vector2f position) {
		this.mPosition = position;
	}

	/**
	 * @return the mWidth
	 */
	public float getWidth() {
		return mWidth;
	}

	/**
	 * @param mWidth
	 *            the mWidth to set
	 */
	public void setWidth(float mWidth) {
		this.mWidth = mWidth;
	}

	/**
	 * @return the mHeight
	 */
	public float getHeight() {
		return mHeight;
	}

	/**
	 * @param mHeight
	 *            the mHeight to set
	 */
	public void setHeight(float mHeight) {
		this.mHeight = mHeight;
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
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return mBitmap;
	}

	/**
	 * @param bitmap
	 *            the bitmap to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}

	/**
	 * @return the mVelocity
	 */
	public Vector2f getVelocity() {
		return mVelocity;
	}

	/**
	 * @param mVelocity the mVelocity to set
	 */
	public void setVelocity(Vector2f velocity) {
		this.mVelocity = velocity;
	}

}
