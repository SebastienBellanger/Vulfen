package com.tojosebe.vulfen.dialog;

import android.graphics.Canvas;

public abstract class DialogDrawArea {

	private int mWidth;

	private int mHeight;

	public abstract void draw(Canvas canvas, int x, int y, int width);

	/**
	 * @return the width
	 */
	public int getWidth() {
		return mWidth;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.mWidth = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return mHeight;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.mHeight = height;
	}

}
