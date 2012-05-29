package com.tojosebe.vulfen.dialog;


public class DialogCounterString implements DialogString {
	
	private int mEndValue;
	
	private TextSize mTextSize;
	
	private int mCurrentAnimationValue = 0;
	
	private int mColor;
	
	private int mCounterAnimationDelay = 100;
	
	private int[] mPadding;
	
	public DialogCounterString(int endValue, TextSize textSize, int color, int[] padding) {
		mEndValue = endValue;
		mTextSize = textSize;
		mColor = color;
		if (padding == null) {
			mPadding = new int[] { 0, 0, 0, 0 };
		} else {
			mPadding = padding;
		}
	}
	
	public void animate(long startTime) {

		if (getCurrentValue() != getEndValue()) {
		
			long timeSinceStart = System.currentTimeMillis() - startTime;
	
			if (timeSinceStart < getCounterAnimationDelay()) {
				return;
			}
	
			int endValue = getEndValue();
			int stepsPerSecond = (int) (endValue / getSecondsToAnimate());
			int steps = (int) ((timeSinceStart / 1000.0f) * stepsPerSecond);
	
			if (steps > endValue) {
				steps = endValue;
			}
	
			setCurrentAnimationValue(steps);
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
