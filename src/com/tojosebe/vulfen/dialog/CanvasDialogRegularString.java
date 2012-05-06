package com.tojosebe.vulfen.dialog;


public class CanvasDialogRegularString implements CanvasDialogString {
	
	private String mContent;
	
	private TextSize mTextSize;

	public CanvasDialogRegularString(String content, TextSize textSize) {
		mContent = content;
		mTextSize = textSize;
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

}
