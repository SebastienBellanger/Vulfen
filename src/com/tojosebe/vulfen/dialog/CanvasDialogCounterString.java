package com.tojosebe.vulfen.dialog;


public class CanvasDialogCounterString implements CanvasDialogString {
	
	private int mEndValue;
	
	private TextSize mTextSize;
	
	private int mCurrentAnimationValue = 0;
	
	private int mColor;
	
	private int mCounterAnimationDelay = 100;
	
	private int[] mPadding;
	
	public CanvasDialogCounterString(int endValue, TextSize textSize, int color, int[] padding) {
		mEndValue = endValue;
		mTextSize = textSize;
		mColor = color;
		if (padding == null) {
			mPadding = new int[] { 0, 0, 0, 0 };
		} else {
			mPadding = padding;
		}
	}

	/**
	 * @return the content
	 */
	@Override
	public String getContent() {
		return "" + mCurrentAnimationValue;
	}

	public int getEndValue() {
		return mEndValue;
	}
	
	public int getCurrentValue() {
		return mCurrentAnimationValue;
	}

	public TextSize getTextSize() {
		return mTextSize;
	}
	
	public void setCurrentAnimationValue(int currentValue) {
		mCurrentAnimationValue = currentValue;
	}
	
	@Override
	public int getColor() {
		return mColor;
	}
	
	public float getSecondsToAnimate() {
		float secondsToAnimate = 2.0f;
		if (mEndValue < 500) {
			secondsToAnimate = 1.0f;
		}
		return secondsToAnimate;
	}

	/**
	 * @return the counterAnimationDelay
	 */
	public int getCounterAnimationDelay() {
		return mCounterAnimationDelay;
	}

	/**
	 * @param counterAnimationDelay the counterAnimationDelay to set
	 */
	public void setCounterAnimationDelay(int counterAnimationDelay) {
		this.mCounterAnimationDelay = counterAnimationDelay;
	}
	
	@Override
	public int[] getPadding() {
		return mPadding;
	}

}
