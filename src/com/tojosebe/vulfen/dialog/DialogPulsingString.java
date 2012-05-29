package com.tojosebe.vulfen.dialog;

public class DialogPulsingString implements DialogString {

	private String mContent;

	private TextSize mTextSize;

	private int mColor;

	private int[] mPadding;

	private float animationLengthMillis = 1000;

	private double textSizeMultiplyer = 1.0d;
	private double minMultiplyer = 0.7d;
	
	private boolean growing = true;

	public DialogPulsingString(String content, TextSize textSize, int color,
			int[] padding) {
		mContent = content;
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
		return mContent;
	}

	public TextSize getTextSize() {
		return mTextSize;
	}

	public double getTextSizeMultiplyer() {
		return textSizeMultiplyer;
	}

	@Override
	public int getColor() {
		return mColor;
	}

	@Override
	public int[] getPadding() {
		return mPadding;
	}

	public void animate(long mDialogStartTime) {

		long totalTime = System.currentTimeMillis() - mDialogStartTime;
		long millisInSequence = totalTime % (long) animationLengthMillis;
		
		long divider = totalTime / (long) animationLengthMillis;
		
		if (divider % 2 == 0) {
			growing = true;
		} else {
			growing = false;
		}
		
		
		textSizeMultiplyer = millisInSequence / (double) animationLengthMillis;

		
		if (growing) {
			textSizeMultiplyer = 1.0 - textSizeMultiplyer;
		}
		
		//multiplyer should be 0.7-1.0:
		textSizeMultiplyer = (1 - minMultiplyer) * textSizeMultiplyer + minMultiplyer;  
		
		
//		textSizeMultiplyer /= 2;
//		textSizeMultiplyer += 0.5;
		
//		double roof = 2 * Math.PI;
//		
//		long totalTime = System.currentTimeMillis() - mDialogStartTime;
//
//		long millisInSequence = totalTime % (long) animationLengthMillis;
//
//		double percentMultiplyer = millisInSequence / (double) animationLengthMillis;
//		
//		textSizeMultiplyer = Math.abs(Math.sin(percentMultiplyer * roof));
//		
//		textSizeMultiplyer /= 2;
//		textSizeMultiplyer += 0.5;
		
		
	}

}
