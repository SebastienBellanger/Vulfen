package com.tojosebe.vulfen.dialog;

public class DialogRegularString implements DialogString {

	private String mContent;

	private TextSize mTextSize;

	private int mColor;

	private int[] mPadding;

	public DialogRegularString(String content, TextSize textSize,
			int color, int[] padding) {
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

	@Override
	public int getColor() {
		return mColor;
	}

	@Override
	public int[] getPadding() {
		return mPadding;
	}

}
