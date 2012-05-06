package com.tojosebe.vulfen.dialog;


public class CanvasDialogCounterString implements CanvasDialogString {
	
	private int mEndValue;
	
	private TextSize mTextSize;
	
	private int mCurrentAnimationValue = 0;
	
	public CanvasDialogCounterString(int endValue, TextSize textSize) {
		mEndValue = endValue;
		mTextSize = textSize;
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

	public TextSize getTextSize() {
		return mTextSize;
	}

}
